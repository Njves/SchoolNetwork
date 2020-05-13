package com.njves.schoolnetwork.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.njves.schoolnetwork.Models.KeyboardUtils
import com.njves.schoolnetwork.Models.network.models.auth.Position
import com.njves.schoolnetwork.Models.network.models.profile.Profile
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.dialog.SelectClassDialog
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.preferences.ProfilePreferences
import com.njves.schoolnetwork.presenter.profile.ProfileNavigator
import com.njves.schoolnetwork.presenter.position.IPosition
import com.njves.schoolnetwork.presenter.profile.IProfile
import com.njves.schoolnetwork.presenter.profile.ProfilePresenter
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.RuntimeException

class ProfileFragment : Fragment(), IProfile, IPosition,
    ProfileNavigator {


    // TODO: При нажатие на подтвердить приложение вылетает с ошибкой о null user
    lateinit var edFN: TextInputEditText
    lateinit var edLN: TextInputEditText
    lateinit var edMN: TextInputEditText
    lateinit var ivAvatar: CircleImageView
    lateinit var btnSubmit: Button
    lateinit var spinnerPosition: Spinner
    lateinit var btnSelectClass: Button
    lateinit var tvProfileStatus: TextView
    
    lateinit var onProfileUpdateListener: OnProfileUpdateListener
    private var profilePresenter = ProfilePresenter(this, this, AuthStorage.getInstance(context), this)
    var schoolClass: String? = null
    var uri: Uri? = null

    companion object {
        const val TAG = "ProfileFragment"
        const val REQUEST_CODE_DIALOG_CLASS_SELECT = 1
        const val SELECT_CLASS_DIALOG = "select_class"
        const val REQUEST_PHOTO_CODE = 2
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnProfileUpdateListener)
            onProfileUpdateListener = context
        else {
            throw RuntimeException(context.toString() + " must implemented OnProfileUpdateListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_profile, container, false)
        // init menu
        setHasOptionsMenu(true)

        // init view
        tvProfileStatus = v.findViewById(R.id.tvProfileStatus)
        edFN = v.findViewById(R.id.edFN)
        edLN = v.findViewById(R.id.edLN)
        edMN = v.findViewById(R.id.edMN)
        ivAvatar = v.findViewById(R.id.ivAvatar)
        spinnerPosition = v.findViewById(R.id.spinnerPosition)
        btnSelectClass = v.findViewById(R.id.btnClassChoice)
        btnSubmit = v.findViewById(R.id.btnSubmit)
        // init data
        profilePresenter.getProfile(AuthStorage.getInstance(context).getUserDetails()!!)
        profilePresenter.getPositions()

        // Контроль показывания кнопки выбора класса
        spinnerPosition.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if((parent?.selectedItem as Position).index==1)
                    btnSelectClass.visibility = View.VISIBLE
                else btnSelectClass.visibility = View.GONE
            }
        }
        // Открытие диалога с выбором класса
        btnSelectClass.setOnClickListener {
            profilePresenter.showDialog(SelectClassDialog())
        }
        ivAvatar.setOnClickListener{
            profilePresenter.requestPhoto(REQUEST_PHOTO_CODE)
        }

        btnSubmit.setOnClickListener {
            val selectedItem = spinnerPosition.selectedItem
            val pos = (selectedItem as Position)
            profilePresenter.makeProfile(
                AuthStorage.getInstance(context).getUserDetails()!!,
                edFN.text.toString(),
                edLN.text.toString(),
                edMN.text.toString(),
                pos.index,
                pos.title,
                schoolClass ?: "0",
                null)
            if(uri!=null) profilePresenter.uploadImage(context!!, uri!!)
            else Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DIALOG_CLASS_SELECT -> {
                    val classNumber = data?.getIntExtra("number", 0)
                    val classChar = data?.getStringExtra("char")
                    schoolClass = "$classNumber$classChar"
                }
                REQUEST_PHOTO_CODE->{
                    uri = data!!.data!!
                    val imageStream = context?.contentResolver?.openInputStream(data?.data!!);
                    val selectedImages = BitmapFactory.decodeStream(imageStream);
                    ivAvatar.setImageBitmap(selectedImages);
                }
            }
        }
    }

    interface OnProfileUpdateListener {
        fun onUpdateProfile()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.action_about)?.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_done -> {
                val selectedItem = spinnerPosition.selectedItem
                val pos = (selectedItem as Position)
                profilePresenter.makeProfile(
                    AuthStorage.getInstance(context).getUserDetails()!!,
                    edFN.text.toString(),
                    edLN.text.toString(),
                    edMN.text.toString(),
                    pos.index,
                    pos.title,
                    schoolClass ?: "0",
                    null)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initView(profile: Profile){
        edFN.setText(profile.firstName)
        edLN.setText(profile.lastName)
        edMN.setText(profile.middleName)
        spinnerPosition.setSelection(profile.position-1)
    }

    override fun onResponseProfile(profile: Profile?) {
        Snackbar.make(view!!, "Профиль успешно обновлен", Snackbar.LENGTH_SHORT).show()
        KeyboardUtils.hideKeyboard(activity!!)
        profile?.let { initView(it) }
        onProfileUpdateListener.onUpdateProfile()
        if (profile != null) {
            initView(profile)
            ProfilePreferences.getInstance(context!!).setIsProfile(true)
        }
    }

    override fun onEmptyProfile() {
        Snackbar.make(view!!, "Профиль пустой", Snackbar.LENGTH_SHORT).show()
    }

    override fun onUpdateProfile(profile: Profile?) {

    }

    override fun onImageUpload() {

    }

    override fun onError(message: String?) {
        Snackbar.make(view!!, "Ошибка: $message", Snackbar.LENGTH_SHORT).show()
        spinnerPosition.setBackgroundColor(Color.RED)
    }

    override fun onFail(t: Throwable) {
        Log.e(TAG, "206 $t")
        Snackbar.make(view!!, "Произошла ошибка сети", Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun onSuccess(positionList: List<Position>) {
        val positionAdapter = ArrayAdapter(
            context!!, R.layout.support_simple_spinner_dropdown_item,
            positionList
        )
        spinnerPosition.adapter = positionAdapter
    }

    override fun showDialogSelect(dialog: DialogFragment) {
        dialog.setTargetFragment(this, REQUEST_CODE_DIALOG_CLASS_SELECT)
        dialog.show(fragmentManager, SELECT_CLASS_DIALOG)
    }

    override fun requestPhoto(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }
}