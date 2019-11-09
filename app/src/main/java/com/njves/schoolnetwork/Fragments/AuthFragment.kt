package com.njves.schoolnetwork.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage

class AuthFragment : Fragment() {

    companion object {

        fun newInstance(): AuthFragment {
            val instance = AuthFragment()
            val bundle = Bundle()
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_auth, container, false)
        // Инициализация view
        val ivLogo = v.findViewById<ImageView>(R.id.ivLogo)
        val btnLogin = v.findViewById<Button>(R.id.btnLogin)
        val tvRegister = v.findViewById<TextView>(R.id.tvRegister)
        btnLogin.setOnClickListener(View.OnClickListener {
            val fragment = LoginFragment()
            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.authContainer, fragment)?.addToBackStack(null)?.commit()
        })

        tvRegister.setOnClickListener(View.OnClickListener {
            val fragment = RegisterFragment()
            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.authContainer, fragment)?.addToBackStack(null)?.commit()
        })

        return v
    }
}