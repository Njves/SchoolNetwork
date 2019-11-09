package com.njves.schoolnetwork.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.njves.schoolnetwork.R

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_login, container, false)
        return v
    }

}