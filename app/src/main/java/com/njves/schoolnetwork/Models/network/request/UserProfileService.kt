package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.USER_PROFILE_GET
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.profile.UserProfile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserProfileService {
    @GET(USER_PROFILE_GET)
    fun getUserProfile(@Query("uid") uid : String) : Call<NetworkResponse<UserProfile>>
}