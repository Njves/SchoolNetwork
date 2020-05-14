package com.njves.schoolnetwork.presenter.settings

import android.content.SharedPreferences
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.preferences.ProfilePreferences

class SettingsPresenter(private val iSettings: ISettings, private val preferences: AuthStorage, private val profilePref: ProfilePreferences, private val navigator: SettingsNavigator) {
    fun logoutFromAccount(){
        preferences.clearUserDetails()
        preferences.setLogged(false)
        profilePref.setIsProfile(false)
        iSettings.onLogout()
    }

    fun navigateToUserSettings(){
        navigator.navigateToUserSettings()
    }

}