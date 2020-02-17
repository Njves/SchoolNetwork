package com.njves.schoolnetwork.Models.network.models.task

import com.njves.schoolnetwork.Models.network.models.auth.User

data class TaskPostModel(val important : Int?, val title : String?, val description : String?, val date : String?, val sender : String?, val receiver : String?)