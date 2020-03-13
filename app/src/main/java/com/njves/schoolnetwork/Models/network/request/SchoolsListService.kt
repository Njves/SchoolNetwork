package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.SCHOOL_LIST_GET
import com.njves.schoolnetwork.Models.network.models.auth.School
import retrofit2.Call
import retrofit2.http.GET

interface SchoolsListService {
    @GET(SCHOOL_LIST_GET)
    fun callSchoolList() : Call<List<School>>
}