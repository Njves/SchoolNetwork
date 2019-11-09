package com.njves.schoolnetwork.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.njves.schoolnetwork.R

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_login, container, false)
        // init views
        val edName = v.findViewById<TextInputEditText>(R.id.edName)
        val edPass = v.findViewById<TextInputEditText>(R.id.edPass)
        val btnSubmit = v.findViewById<Button>(R.id.btnLogin)
        val btnForgotPass = v.findViewById<TextView>(R.id.tvForgotPass)

        return v
    }

}