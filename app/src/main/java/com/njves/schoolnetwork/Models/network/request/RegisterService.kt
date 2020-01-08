package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.auth.AuthResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("API/Auth/register.php")
    fun callRegister(@Body user : User) : Call<AuthResponse>
}