package com.njves.schoolnetwork.presenter.profile

import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.presenter.IError

interface IProfile : IError {
    fun onSuccessGet(profile : Profile?)
    fun onSuccessPost(profile : Profile?)
}