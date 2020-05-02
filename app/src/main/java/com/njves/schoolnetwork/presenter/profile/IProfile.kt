package com.njves.schoolnetwork.presenter.profile

import com.njves.schoolnetwork.Models.network.models.profile.Profile
import com.njves.schoolnetwork.presenter.IError

interface IProfile : IError {
    fun onProfileFilled(profile : Profile?)
    fun onProfileEmpty()
}