package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.auth.AuthResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("API/Auth/login.php")
    fun callLogin(@Query("name") name : String, @Query("password") pass : String) : Call<AuthResponse>
}