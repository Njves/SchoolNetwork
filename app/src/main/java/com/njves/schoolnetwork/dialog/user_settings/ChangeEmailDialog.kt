package com.njves.schoolnetwork.dialog.user_settings

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.fragments.settings.UserSettingsFragment

class ChangeEmailDialog : DialogFragment() {
    private lateinit var tvCurrentEmail: TextView
    private lateinit var edNewEmail: EditText
    private lateinit var btnSubmit: Button
    private var email: String? = null
    companion object{
        public const val EMAIL_ARG = "email"
        public const val EMAIL_RESULT = "newEmail"
        fun newInstance(email: String): ChangeEmailDialog {
            val bundle = Bundle()
            bundle.putString(EMAIL_ARG, email)
            val instance = ChangeEmailDialog()
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments!!.getString(EMAIL_ARG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_change_email, null)
        val builder = AlertDialog.Builder(context)
        tvCurrentEmail = view.findViewById(R.id.tvCurrentEmail)
        edNewEmail = view.findViewById(R.id.edNewEmail)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        tvCurrentEmail.text = email?:""
        btnSubmit.setOnClickListener{
            val newEmail = edNewEmail.text.toString()
            val intent = Intent()
            intent.putExtra(EMAIL_RESULT, newEmail)
            val target = targetFragment
            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        }
        builder.setView(view)
        return builder.create()
    }
}