package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName
import com.njves.schoolnetwork.Models.network.models.auth.User

data class AuthResponse(val code : Int, val message : String, @SerializedName("objects") val data : User)