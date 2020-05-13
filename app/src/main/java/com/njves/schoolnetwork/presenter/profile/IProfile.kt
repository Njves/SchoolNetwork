package com.njves.schoolnetwork.presenter.profile

import com.njves.schoolnetwork.Models.network.models.profile.Profile
import com.njves.schoolnetwork.presenter.IError

interface IProfile : IError {
    fun onResponseProfile(profile: Profile?)
    fun onEmptyProfile()
    fun onUpdateProfile(profile: Profile?)
    fun onImageUpload()
}