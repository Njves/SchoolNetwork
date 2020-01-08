package com.njves.schoolnetwork.Models.network.models.auth

import com.google.gson.annotations.SerializedName

data class School(@SerializedName("id") val index : Int, val title : String)
{
    override fun toString(): String {
        return "$index.$title"
    }
}
