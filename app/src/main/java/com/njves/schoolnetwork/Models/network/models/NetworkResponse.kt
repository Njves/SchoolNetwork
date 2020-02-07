package com.njves.schoolnetwork.Models.network.models

import com.google.gson.annotations.SerializedName
import com.njves.schoolnetwork.Models.network.models.auth.User

data class NetworkResponse<T>(val code : Int, val message : String, @SerializedName("objects") val data : T?)