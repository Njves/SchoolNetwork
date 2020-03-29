package com.njves.schoolnetwork.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.callback.UpdateToolbarTitleListener
import java.lang.RuntimeException

class AuthFragment : Fragment() {
    lateinit var updateToolbarTitleListener: UpdateToolbarTitleListener
    companion object {

        fun newInstance(): AuthFragment {
            val instance = AuthFragment()
            val bundle = Bundle()
            instance.arguments = bundle
            return instance
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is UpdateToolbarTitleListener)updateToolbarTitleListener = context
        else throw RuntimeException(" must implemented UpdateTitleListener")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_auth, container, false)
        updateToolbarTitleListener.updateActionBar(getString(R.string.app_name))
        // Инициализация view
        val ivLogo = v.findViewById<ImageView>(R.id.ivLogo)
        val btnLogin = v.findViewById<Button>(R.id.btnLogin)
        val tvRegister = v.findViewById<TextView>(R.id.tvRegister)
        btnLogin.setOnClickListener {
            val fragment = LoginFragment()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.authContainer, fragment)?.addToBackStack(null)?.commit()
        }

        tvRegister.setOnClickListener {
            val fragment = RegisterFragment()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.authContainer, fragment)?.addToBackStack(null)?.commit()
        }

        return v
    }
}