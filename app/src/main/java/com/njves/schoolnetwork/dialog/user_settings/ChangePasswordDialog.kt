package com.njves.schoolnetwork.dialog.user_settings

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.fragments.settings.UserSettingsFragment
import com.njves.schoolnetwork.presenter.settings.user.UserSettingsPresenter

class ChangePasswordDialog : DialogFragment() {
    var password: String? = null
    companion object{
        private const val PASSWORD_ARG = "password"
        public const val PASSWORD_RESULT = "new_password"
        fun newInstance(password: String): ChangePasswordDialog{
            val bundle = Bundle()
            bundle.putString(PASSWORD_ARG, password)
            val instance = ChangePasswordDialog()
            instance.arguments = bundle
            return instance
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_change_password, null)
        val builder = AlertDialog.Builder(context)
        val edOldPass = view?.findViewById<TextInputEditText>(R.id.edOldPass)
        val edNewPass = view?.findViewById<TextInputEditText>(R.id.edNewPass)
        val edNewPassRetry = view?.findViewById<TextInputEditText>(R.id.edNewPassRetry)

        builder.setView(view)
        builder.setPositiveButton(R.string.action_submit
        ) { dialog, which ->
            val oldPass = edOldPass!!.text.toString()
            val newPass = edNewPass!!.text.toString()
            val newPassRetry = edNewPassRetry!!.text.toString()
            val intent = Intent()
            intent.putExtra(PASSWORD_RESULT,arrayOf(oldPass, newPass, newPassRetry))
            targetFragment!!.onActivityResult(UserSettingsFragment.PASSWORD_DIALOG, Activity.RESULT_OK, intent)
        }

        return builder.create()
    }
}