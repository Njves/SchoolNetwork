package com.njves.schoolnetwork.Models.network.models

import com.google.gson.annotations.SerializedName

data class NetworkResponse<T>(val code : Int, val message : String, @SerializedName("objects")val data : T){
    companion object{
        public const val SUCCESS_RESPONSE = 0
        public const val ERROR_RESPONSE = 1

    }
}