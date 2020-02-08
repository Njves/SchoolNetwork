package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import retrofit2.Call
import retrofit2.http.GET

interface TeachersService {
    @GET("teacher.get")
    fun getTeacherList(schoolNumber : Int) : Call<NetworkResponse<String>>
}