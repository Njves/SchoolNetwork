package com.njves.schoolnetwork.presenter.settings

import android.content.SharedPreferences
import com.njves.schoolnetwork.preferences.AuthStorage

class SettingsPresenter(private val iSettings: ISettings,private val preferences: AuthStorage, private val navigator: SettingsNavigator) {
    fun logoutFromAccount(){
        preferences.clearUserDetails()
        preferences.setLogged(false)
        preferences.setIsProfile(false)
        iSettings.onLogout()
    }

    fun navigateToUserSettings(){
        navigator.navigateToUserSettings()
    }

}