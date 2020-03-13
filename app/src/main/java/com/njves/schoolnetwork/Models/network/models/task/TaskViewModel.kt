package com.njves.schoolnetwork.Models.network.models.task

import com.njves.schoolnetwork.Models.network.models.auth.Profile


data class TaskViewModel(val important : Int, val title : String, val description : String, val date : String, val sender : Profile, val receiver : String, val uid : String)