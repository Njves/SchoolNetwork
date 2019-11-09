package com.njves.schoolnetwork.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.njves.schoolnetwork.Fragments.AuthFragment
import com.njves.schoolnetwork.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val authFragment = AuthFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.authContainer, authFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

    }
}
