package com.njves.schoolnetwork.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.dialog.ClassChoiceDialog
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.presenter.position.IPosition
import com.njves.schoolnetwork.presenter.position.PositionPresenter
import com.njves.schoolnetwork.presenter.profile.IProfile
import com.njves.schoolnetwork.presenter.profile.ProfilePresenter

class ProfileFragment : Fragment(), IProfile, IPosition {
    // TODO: При нажатие на подтвердить приложение вылетает с ошибкой о null user
    lateinit var edFN: TextInputEditText
    lateinit var edLN: TextInputEditText
    lateinit var edMN: TextInputEditText
    lateinit var ivAvatar: ImageView
    lateinit var btnSubmit: Button
    lateinit var spinnerPosition: Spinner
    lateinit var btnClassChoice: Button
    lateinit var tvProfileStatus: TextView
    lateinit var onAuthPassedListener: OnAuthPassedListener
    lateinit var onProfileUpdateListener: OnProfileUpdateListener
    private var profilePresenter = ProfilePresenter(this)
    private var positionPresenter = PositionPresenter(this)
    var schoolClass: String? = null

    companion object {
        const val TAG = "ProfileFragment"
        const val REQUEST_CODE_DIALOG_CLASS_CHOICE = 1
        fun newInstance(user: String): Fragment {
            val fragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putString("user", user)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAuthPassedListener)
            onAuthPassedListener = context
        if (context is OnProfileUpdateListener)
            onProfileUpdateListener = context
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
        btnClassChoice = v.findViewById(R.id.btnClassChoice)
        btnSubmit = v.findViewById(R.id.btnSubmit)
        // init data
        init()

        // Контроль показывания кнопки выбора класса
        spinnerPosition.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if((parent?.selectedItem as Position).index==1)
                    btnClassChoice.visibility = View.VISIBLE
                else btnClassChoice.visibility = View.GONE
                val item = spinnerPosition.selectedItem as Position
                Toast.makeText(context, "${item.index} ${item.title}", Toast.LENGTH_SHORT).show()

            }

        }


        // Открытие диалога с выбором класса
        btnClassChoice.setOnClickListener {
            val dialog: DialogFragment = ClassChoiceDialog()
            dialog.setTargetFragment(this, REQUEST_CODE_DIALOG_CLASS_CHOICE)
            dialog.show(fragmentManager, "")
        }


        btnSubmit.setOnClickListener {
            sendData()
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DIALOG_CLASS_CHOICE -> {
                    val classNumber = data?.getIntExtra("number", 0)
                    val classChar = data?.getStringExtra("char")
                    schoolClass = "$classNumber$classChar"

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
                sendData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendData() {
        val storage = AuthStorage(context)
        // Проверка на успешность запроса
        // TODO: Заменить position
        val item = spinnerPosition.selectedItem
        val index = (item as Position).index
        Log.d(TAG, item.toString())
        val request = Profile(
            storage.getUserDetails(),
            edFN.text.toString(),
            edLN.text.toString(),
            edMN.text.toString(),
            index,
            null,
            schoolClass ?: "0",
            null,
            null)
        profilePresenter.postProfile("UPDATE", request)
    }
    private fun init() {
        val storage = AuthStorage(context)
        profilePresenter.getProfile(storage.getUserDetails()!!)
        positionPresenter.getPositions()

    }
    private fun initView(profile: Profile){
        edFN.setText(profile.firstName)
        edLN.setText(profile.lastName)
        edMN.setText(profile.middleName)
        spinnerPosition.setSelection(profile.position-1)
    }
    override fun onSuccessGet(profile: Profile?) {
        profile?.let { initView(it) }
    }

    override fun onSuccessPost(profile: Profile?) {
        Snackbar.make(view!!, "Профиль успешно обновлен", Snackbar.LENGTH_SHORT).show()
        KeyboardUtils.hideKeyboard(activity!!)
        profile?.let { initView(it) }
        onProfileUpdateListener.onUpdateProfile()
    }

    override fun onError(message: String) {
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
}