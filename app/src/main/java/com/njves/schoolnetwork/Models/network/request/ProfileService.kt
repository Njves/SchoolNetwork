package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.*
import com.njves.schoolnetwork.Models.network.models.NetworkResponse

import com.njves.schoolnetwork.Models.network.models.profile.Profile

import com.njves.schoolnetwork.Models.network.models.profile.ProfileWrapper
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {

    @POST(PROFILE_POST)
    fun postProfile(@Body profile: ProfileWrapper): Call<NetworkResponse<Profile?>>

    @GET(PROFILE_GET)
    fun getProfile(@Query("type") type : String,@Query("uid") uid : String)  : Call<NetworkResponse<Profile?>>

    @POST(PROFILE_UPDATE)
    fun updateProfile(@Body profile : ProfileWrapper) : Call<NetworkResponse<Profile>>

    @Multipart
    @POST("api/profile/avatar.php")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ResponseBody>
}
