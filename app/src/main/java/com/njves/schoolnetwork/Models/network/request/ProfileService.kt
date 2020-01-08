package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.auth.AuthResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {
    @POST("API/Profile")

    fun callProfile(@Body profile: Profile, @Header("Authorization") credentials: String): Call<AuthResponse>

    @GET("")
    fun getProfile(@Query("uid") uid : String)
}