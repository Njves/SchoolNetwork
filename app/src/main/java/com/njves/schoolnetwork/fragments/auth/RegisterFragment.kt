package com.njves.schoolnetwork.fragments.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.callback.UpdateToolbarTitleListener
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.dialog.ErrorDialog
import com.njves.schoolnetwork.presenter.auth.IAuth
import com.njves.schoolnetwork.presenter.auth.RegisterPresenter
import com.njves.schoolnetwork.presenter.school.ISchool
import com.njves.schoolnetwork.presenter.school.SchoolPresenter

class RegisterFragment : Fragment(), IAuth, ISchool {

    private lateinit var onAuthPassedListener: OnAuthPassedListener
    private lateinit var updateToolbarTitleListener: UpdateToolbarTitleListener
    private lateinit var edName: TextInputEditText
    private lateinit var edEmail: TextInputEditText
    private lateinit var edPass: TextInputEditText
    private lateinit var edPassRetry: TextInputEditText
    private lateinit var fabNext: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private lateinit var schoolAdapter: ArrayAdapter<School>
    private lateinit var spinnerSchool: Spinner
    private lateinit var checkLicense: CheckedTextView
    private lateinit var presenter: RegisterPresenter
    companion object {
        const val TAG = "RegisterFragment"
        const val BUNDLE_SCHOOL_NUMBER = "schoolNumber"
        const val BUNDLE_NAME = "name"
        const val BUNDLE_EMAIL = "email"

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAuthPassedListener) {
            onAuthPassedListener = context

        } else {
            throw RuntimeException(context.toString() + " must implement OnAuthPassedListener")
        }
        if (context is UpdateToolbarTitleListener) {
            updateToolbarTitleListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement UpdateToolbarTitleListener")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // Сохраняем состояние данных в полях
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_NAME, edName.text.toString())
        outState.putString(BUNDLE_EMAIL, edEmail.text.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_register, container, false)
        if (savedInstanceState != null) {
            val savedName = savedInstanceState.getString(BUNDLE_NAME, "")
            val savedEmail = savedInstanceState.getString(BUNDLE_EMAIL, "")
        }
        setHasOptionsMenu(true)
        presenter = RegisterPresenter(this)
        updateToolbarTitleListener.updateActionBar(getString(R.string.auth_register))
        SchoolPresenter(this).getSchool()
        progressBar = v.findViewById(R.id.progressBar)
        spinnerSchool = v.findViewById(R.id.spinnerSchoolNumber)
        checkLicense = v.findViewById(R.id.checkLicense)
        checkLicense.setOnClickListener { checkLicense.isChecked = !checkLicense.isChecked }

        // Создаем адаптер для спиннера
        schoolAdapter = ArrayAdapter<School>(context!!, R.layout.support_simple_spinner_dropdown_item)

        edName = v.findViewById(R.id.edName)
        edEmail = v.findViewById(R.id.edEmail)
        edPass = v.findViewById(R.id.edPass)
        edPassRetry = v.findViewById(R.id.edPassRetry)

        fabNext = v.findViewById(R.id.fabNext)
        fabNext.hide()
        fabNext.setOnClickListener{
            presenter.register(User(null,
                edName.text.toString(),
                edEmail.text.toString(),
                edPass.text.toString(),
                edPassRetry.text.toString(),
                schoolAdapter.getItem(spinnerSchool.selectedItemPosition)!!))
        }
        return v
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu!!.findItem(R.id.action_about).isVisible = false

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_done -> {
                presenter.register(User(null,
                    edName.text.toString(),
                    edEmail.text.toString(),
                    edPass.text.toString(),
                    edPassRetry.text.toString(),
                    schoolAdapter.getItem(spinnerSchool.selectedItemPosition)!!))
            }
            R.id.home -> {
                fragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSuccess(user: User?) {
        onAuthPassedListener.onSuccess(user!!.uid)
    }

    override fun onError(message: String?) {
        Log.d("RegisterFragment", "Error: $message")
        val errorDialog = ErrorDialog.newInstance(message)
        errorDialog.show(activity?.supportFragmentManager, "errorDialog")
    }

    override fun onFail(t: Throwable) {
        Log.e("RegisterFragment", "Throwable: $t")
        val errorDialog = ErrorDialog.newInstance("Произошла ошибка сети")
        errorDialog.show(activity?.supportFragmentManager,"errorDialog")
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onSuccess(positionList: List<School>) {
        schoolAdapter.addAll(positionList)
        spinnerSchool.adapter = schoolAdapter
    }

    override fun onCheckChange(isCheck: Boolean) {
        // TODO: ТУт остановился
        val tvErrorMsg = view?.findViewById<TextView>(R.id.tvErrorCheck)
        tvErrorMsg?.visibility = View.VISIBLE
    }
}




