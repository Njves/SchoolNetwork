package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName

data class Position(@SerializedName("id") val index : Int, @SerializedName("position_title") val title : String){
    override fun toString(): String {
        return title
    }
}