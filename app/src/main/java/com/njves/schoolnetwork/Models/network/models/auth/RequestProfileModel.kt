package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName
import com.njves.schoolnetwork.Models.network.models.profile.UserProfile

data class RequestProfileModel (val type : String, @SerializedName("objects")val data : UserProfile)