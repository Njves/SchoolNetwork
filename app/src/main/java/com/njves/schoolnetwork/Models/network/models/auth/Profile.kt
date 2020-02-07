package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName

data class Profile(val uid : String?, @SerializedName("first_name")val firstName : String, @SerializedName("last_name") val lastName : String, @SerializedName("middle_name") val middleName : String, val position : Int, val `class` : String)