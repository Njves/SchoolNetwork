package com.njves.schoolnetwork.fragments.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.callback.UpdateToolbarTitleListener
import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import com.njves.schoolnetwork.presenter.auth.IAuth
import com.njves.schoolnetwork.presenter.auth.LoginPresenter

class LoginFragment : Fragment(), IAuth {
    private lateinit var edName : TextInputEditText
    private lateinit var edPass : TextInputEditText
    private lateinit var btnSubmit : Button
    private lateinit var btnForgotPass : Button
    private lateinit var progressBar : ProgressBar
    private lateinit var checkboxRemMe : CheckedTextView
    private lateinit var onAuthPassedListener : OnAuthPassedListener
    private lateinit var updateToolbarTitleListener : UpdateToolbarTitleListener

    companion object{
        const val TAG = "LoginFragment"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAuthPassedListener) {
            onAuthPassedListener = context
        } else {
            throw RuntimeException("$context must implement OnAuthPassedListener")
        }
        if(context is UpdateToolbarTitleListener) {
            updateToolbarTitleListener = context
        }
        else{
            throw RuntimeException("$context must implement UpdateToolbarTitleListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_login, container, false)
        updateToolbarTitleListener.updateActionBar(getString(R.string.auth_login))
        // init views
        edName = v.findViewById(R.id.edName)
        edPass = v.findViewById(R.id.edPass)
        btnSubmit = v.findViewById(R.id.btnSubmit)
        checkboxRemMe = v.findViewById(R.id.checkboxRemMe)
        btnForgotPass = v.findViewById(R.id.tvForgotPass)
        progressBar = v.findViewById(R.id.progressBar)

        checkboxRemMe.setOnClickListener { checkboxRemMe.isChecked = !checkboxRemMe.isChecked }
        val loginPresenter = LoginPresenter(this)
        val storage = AuthStorage(context)
        edName.setText(storage.getUserName()?:"")
        btnSubmit.setOnClickListener {
            loginPresenter.login(edName.text.toString(), edPass.text.toString())
        }
        return v
    }

    override fun onSuccess(user: User?) {
        if(checkboxRemMe.isChecked) {
            AuthStorage(context).setUserName(user?.name?:"")
        }
        onAuthPassedListener.onSuccess(user?.uid)
    }

    override fun onError(message: String?) {
        AuthErrorDialog.newInstance(message).show(childFragmentManager, null)
    }

    override fun onFail(t: Throwable) {
        Log.d(TAG, t.toString())
        Toast.makeText(context, "Произошла ошибка запроса", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }


}