package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.LOGIN
import com.njves.schoolnetwork.Models.network.REGISTER
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @GET(LOGIN)
    fun callLogin(@Query("name") name : String, @Query("password") pass : String) : Call<NetworkResponse<User>>

    @POST(REGISTER)
    fun callRegister(@Body user : User) : Call<NetworkResponse<User>>
}