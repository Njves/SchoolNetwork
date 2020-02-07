package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.task.Task

data class RequestProfileModel (val type : String, @SerializedName("objects")val data : Profile)