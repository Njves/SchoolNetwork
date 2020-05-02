package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.TEACHERS_GET
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.profile.Profile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TeachersService {
    @GET(TEACHERS_GET)
    fun getTeacherList(@Query("position") pos : Int, @Query("schoolNumber") sm : Int) : Call<NetworkResponse<List<Profile>>>
}