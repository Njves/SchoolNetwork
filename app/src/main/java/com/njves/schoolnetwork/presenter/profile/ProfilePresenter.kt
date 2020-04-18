package com.njves.schoolnetwork.presenter.profile

import android.util.Log
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.NetworkService.Companion.TYPE_POST
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.profile.ProfileWrapper
import com.njves.schoolnetwork.Models.network.request.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ProfilePresenter(val iProfile: IProfile) {
    private var retrofit: Retrofit = NetworkService.instance.getRetrofit()
    private var profileService = retrofit.create(ProfileService::class.java)
    fun postProfile(type : String,profile : Profile){
        val postCall = profileService.postProfile(ProfileWrapper(type, profile))
        postCall.enqueue(object : Callback<NetworkResponse<Profile>>{
            override fun onResponse(call: Call<NetworkResponse<Profile>>, response: Response<NetworkResponse<Profile>>) {
                val code = response.body()?.code
                val message = response.body()?.message
                val profile = response.body()!!.data
                if (code == NetworkResponse.SUCCESS_RESPONSE) {
                    iProfile.onSuccessPost(profile)
                } else {
                    iProfile.onError(message!!)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
                iProfile.onFail(t)
            }
        })
    }



    fun getProfile(uid : String){
        val getCall = profileService.getProfile(NetworkService.TYPE_GET, uid)
        getCall.enqueue(object : Callback<NetworkResponse<Profile>>{
            override fun onResponse(call: Call<NetworkResponse<Profile>>, response: Response<NetworkResponse<Profile>>) {
                val code = response.body()?.code
                val message = response.body()?.message
                val profile = response.body()!!.data
                if (code == NetworkResponse.SUCCESS_RESPONSE) {
                    iProfile.onSuccessGet(profile)
                } else {
                    iProfile.onError(message!!)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
                iProfile.onFail(t)
            }

        })

    }
}