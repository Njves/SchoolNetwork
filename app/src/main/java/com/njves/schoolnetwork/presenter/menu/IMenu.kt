package com.njves.schoolnetwork.presenter.menu

import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.presenter.IError

interface IMenu : IError{
    fun onSuccess(profile: Profile)
}