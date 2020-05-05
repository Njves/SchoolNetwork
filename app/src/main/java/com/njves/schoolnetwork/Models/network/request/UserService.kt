package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("api/user")
    fun getUser(@Query("type")type : String,@Query("uid") uid: String) : Call<NetworkResponse<User>>

    @POST("api/user.php")
    @FormUrlEncoded
    fun updateEmail(@FieldMap map: HashMap<String, String>) : Call<NetworkResponse<Void>>

    @POST("api/user.php")
    @FormUrlEncoded
    fun updatePassword(@FieldMap map: HashMap<String, String>): Call<NetworkResponse<Void>>

    @POST("api/user.php")
    @FormUrlEncoded
    fun updateSchool(@FieldMap map: HashMap<String, String>): Call<NetworkResponse<Void>>
}