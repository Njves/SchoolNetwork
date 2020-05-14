package com.njves.schoolnetwork.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.njves.schoolnetwork.Activity.MainActivity
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.preferences.ProfilePreferences
import com.njves.schoolnetwork.presenter.settings.ISettings
import com.njves.schoolnetwork.presenter.settings.SettingsNavigator
import com.njves.schoolnetwork.presenter.settings.SettingsPresenter

class SettingsFragment : Fragment(), ISettings, SettingsNavigator {
    private lateinit var preferences : AuthStorage
    private lateinit var presenter : SettingsPresenter
    private lateinit var btnLogout: Button
    private lateinit var btnUserSettings: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        btnLogout = v.findViewById(R.id.btnLogout)
        btnUserSettings = v.findViewById(R.id.btnAccount)
        preferences = AuthStorage(context!!)
        presenter = SettingsPresenter(this, preferences,ProfilePreferences.getInstance(context), this)
        btnLogout.setOnClickListener{
            presenter.logoutFromAccount()
        }
        btnUserSettings.setOnClickListener{
            presenter.navigateToUserSettings()
        }
        return v
    }

    override fun onLogout() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun navigateToUserSettings() {
        findNavController().navigate(R.id.action_nav_settings_to_nav_settings_user)
    }
}
