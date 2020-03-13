package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("uid_key") val uid : String?,
                   @SerializedName("first_name")val firstName : String,
                   @SerializedName("last_name") val lastName : String,
                   @SerializedName("middle_name") val middleName : String,
                   val position : Int,
                   @SerializedName("position_title") val positionTitle : String?,
                   val `class` : String,
                   @SerializedName("class_value") val classValue : String?,
                    @SerializedName("avatar_link") val avatarLink : String?)