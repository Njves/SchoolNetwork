package com.njves.schoolnetwork.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.dialog.user_settings.ChangeEmailDialog
import com.njves.schoolnetwork.dialog.user_settings.ChangePasswordDialog
import com.njves.schoolnetwork.dialog.user_settings.ChangeSchoolDialog
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.presenter.settings.user.IUserSettings
import com.njves.schoolnetwork.presenter.settings.user.UserSettingsNavigator
import com.njves.schoolnetwork.presenter.settings.user.UserSettingsPresenter

class UserSettingsFragment : Fragment(), IUserSettings, UserSettingsNavigator {
    private lateinit var tvChangeEmail: TextView
    private lateinit var tvChangePassword: TextView
    private lateinit var tvChangeSchool: TextView
    private lateinit var presenter: UserSettingsPresenter
    private var dialog: DialogFragment? = null
    private var user: User? = null
    companion object{
        const val TAG = "UserSettingsFragment"
        const val EMAIL_DIALOG = 1
        const val PASSWORD_DIALOG = 2
        const val SCHOOL_DIALOG = 3
    }
    init{

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)
        presenter = UserSettingsPresenter(this, this, AuthStorage(context))
        tvChangeEmail = view.findViewById(R.id.tvChangeEmail)
        tvChangePassword = view.findViewById(R.id.tvChangePassword)
        tvChangeSchool = view.findViewById(R.id.tvChangeSchool)
        presenter.getUser()
        tvChangeEmail.setOnClickListener{
            presenter.showEmailDialog(user?.email!!)
        }
        tvChangePassword.setOnClickListener{
            
        }
        tvChangeSchool.setOnClickListener{

        }


        return view
    }

    override fun onUserReceive(user: User) {
        this.user = user
    }

    override fun showDialog() {

    }

    override fun onError(message: String?) {
        if(message != null)
        Snackbar.make(view!!, message, Snackbar.LENGTH_INDEFINITE).show()

    }

    override fun onFail(t: Throwable) {
        Snackbar.make(view!!, "Неизвестная ошибка", Snackbar.LENGTH_INDEFINITE).show()
        Log.wtf(TAG, t.toString())
        findNavController().navigateUp()
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun showEmailDialog(email: String) {
        dialog = ChangeEmailDialog.newInstance(email)
        dialog?.setTargetFragment(this, EMAIL_DIALOG)
        dialog?.show(fragmentManager, "email_dialog")
    }

    override fun showPasswordDialog(password: String) {
        dialog = ChangePasswordDialog.newInstance(password)
        dialog?.setTargetFragment(this, PASSWORD_DIALOG)
        dialog?.show(fragmentManager, "password_dialog")
    }

    override fun showSchoolDialog(school: String) {
        dialog = ChangeSchoolDialog.newInstance(12)
        dialog?.setTargetFragment(dialog, SCHOOL_DIALOG)
        dialog?.show(fragmentManager, "school_dialog")
    }

    override fun onEmailUpdate() {

    }

    override fun onPasswordUpdate() {

    }

    override fun onSchoolUpdate() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            EMAIL_DIALOG->{
                val email = data?.getStringExtra(ChangeEmailDialog.EMAIL_RESULT)
                presenter.updateEmail(user!!.uid!!, email!!)

            }
            PASSWORD_DIALOG->{
                val password = data?.getStringExtra(ChangePasswordDialog.PASSWORD_RESULT)
                presenter.updatePassword(user!!.uid!!, "password","fefew", "fefew")
            }
            SCHOOL_DIALOG->{
                val school = data?.getStringExtra(ChangeSchoolDialog.SCHOOL_RESULT)
                presenter.updateSchool(user!!.uid!!, school!!)
            }
        }
    }
}