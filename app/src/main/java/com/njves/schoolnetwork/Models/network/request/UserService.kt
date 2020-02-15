package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    // {"type":"GET", "position":1}
    @GET("TaskPostModel/users?type=GET&school=12&position=1")
    fun getUsersByPosition(@Query("type") type : String,
                           @Query("position") position : Int,
                           @Query("schoolNumber") schoolNumber : Int) : Call<List<User>>
    @GET("API/users.php")
    fun getUser(@Query("uid") uid : String) : Call<NetworkResponse<User>>
}