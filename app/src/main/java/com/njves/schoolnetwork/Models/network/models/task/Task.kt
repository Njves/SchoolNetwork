package com.njves.schoolnetwork.Models.network.models.task

import com.njves.schoolnetwork.Models.network.models.auth.Profile


data class Task(val important : Int, val title : String, val description : String, val date : Long, val sender : Profile, val receiver : Profile, val uid : String?)