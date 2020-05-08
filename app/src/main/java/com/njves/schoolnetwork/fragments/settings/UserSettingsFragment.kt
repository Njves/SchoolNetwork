package com.njves.schoolnetwork.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.njves.schoolnetwork.Models.network.models.auth.School
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
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserSchool: TextView
    private lateinit var presenter: UserSettingsPresenter
    private var dialog: DialogFragment? = null
    private var fragment: Fragment? = null
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
        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        tvUserSchool = view.findViewById(R.id.tvUserSchool)
        presenter.getUser()
        tvChangeEmail.setOnClickListener{
            presenter.showEmailDialog(user?.email!!)
        }
        tvChangePassword.setOnClickListener{
            presenter.showPasswordDialog(user?.password!!)
        }
        tvChangeSchool.setOnClickListener{
            presenter.showSchoolDialog(user?.schoolNumber!!.index)
        }


        return view
    }

    override fun onUserReceive(user: User) {
        this.user = user
    }


    override fun onError(message: String?) {
        if(message != null)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

    override fun onFail(t: Throwable) {
        Toast.makeText(context, "Ошибка сервера", Toast.LENGTH_LONG).show()
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

    override fun showSchoolDialog(school: Int) {
        dialog = ChangeSchoolDialog.newInstance(school)
        dialog?.setTargetFragment(this, SCHOOL_DIALOG)
        dialog?.show(fragmentManager, "school_dialog")
    }

    override fun onEmailUpdate() {
        Snackbar.make(view!!, "Почта успешно обновлено", Snackbar.LENGTH_SHORT).show()
        presenter.getUser()
    }

    override fun onPasswordUpdate() {
        Snackbar.make(view!!, "Пароль успешно обновлен", Snackbar.LENGTH_SHORT).show()
        presenter.getUser()
    }

    override fun onSchoolUpdate() {
        Snackbar.make(view!!, "Школа успешно обновлен", Snackbar.LENGTH_SHORT).show()
        presenter.getUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            EMAIL_DIALOG->{
                val email = data?.getStringExtra(ChangeEmailDialog.EMAIL_RESULT)
                presenter.updateEmail(user!!.uid!!, email!!)

            }
            PASSWORD_DIALOG->{
                val arrayPassword = data?.getStringArrayExtra(ChangePasswordDialog.PASSWORD_RESULT)!!
                presenter.updatePassword(user!!.uid!!, arrayPassword[0],arrayPassword[1], arrayPassword[2])
            }
            SCHOOL_DIALOG->{
                val school = data?.getIntExtra(ChangeSchoolDialog.SCHOOL_RESULT,0)
                presenter.updateSchool(user!!.uid!!, school!!)
            }
        }
    }
    override fun onViewFill(name: String, email: String, school: School) {
        tvUserName.text = resources.getString(R.string.settings_info_user_name, name)
        tvUserEmail.text = resources.getString(R.string.settings_info_user_email, email)
        tvUserSchool.text = resources.getString(R.string.settings_info_user_school, school.title)
    }
}