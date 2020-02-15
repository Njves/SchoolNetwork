package com.njves.schoolnetwork.Models.network.models.task

import com.google.gson.annotations.SerializedName

data class RequestTaskModel(val type : String, @SerializedName("objects")val body : TaskPostModel)