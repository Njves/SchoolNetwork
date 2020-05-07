package com.njves.schoolnetwork.presenter.settings.user

import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.presenter.IError

interface IUserSettings : IError {
    fun onUserReceive(user: User)
    fun onEmailUpdate()
    fun onPasswordUpdate()
    fun onSchoolUpdate()


}