package com.njves.schoolnetwork.Models.network.models.auth

data class User(val uid : String?,val name : String,val email : String, val password : String, val passwordRetry : String, val schoolNumber : School)