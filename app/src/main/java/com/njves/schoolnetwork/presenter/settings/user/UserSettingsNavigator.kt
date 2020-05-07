package com.njves.schoolnetwork.presenter.settings.user

interface UserSettingsNavigator {
    fun showEmailDialog(email: String)
    fun showPasswordDialog(password: String)
    fun showSchoolDialog(school: Int)
}