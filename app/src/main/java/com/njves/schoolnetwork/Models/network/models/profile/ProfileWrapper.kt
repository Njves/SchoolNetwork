package com.njves.schoolnetwork.Models.network.models.profile

import com.google.gson.annotations.SerializedName
import com.njves.schoolnetwork.Models.network.models.auth.Profile

data class ProfileWrapper(val type : String, @SerializedName("objects") val profile : Profile)