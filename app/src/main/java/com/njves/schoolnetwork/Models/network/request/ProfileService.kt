package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.RequestProfileModel
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.profile.UserProfile
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {



    @POST("API/Profile/index.php")
    fun postProfile(@Body profile: RequestProfileModel): Call<NetworkResponse<UserProfile>>

    @GET("API/Profile/index.php")
    fun getProfile(@Query("type") type : String,@Query("uid") uid : String)  : Call<NetworkResponse<Profile>>
    
    @POST("API/Profile/index.php")
    fun postUpdateProfile(@Field("type") type : String, @Body profile : Profile, @Header("Authorization") credentials: String) : Call<NetworkResponse<Profile>>
}
