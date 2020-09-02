package com.njves.schoolnetwork.presenter.auth

import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.presenter.IError
import com.njves.schoolnetwork.presenter.IView

interface IAuth : IError {
    fun onSuccess(user : User?)
    fun onCheckChange(isCheck: Boolean)
}