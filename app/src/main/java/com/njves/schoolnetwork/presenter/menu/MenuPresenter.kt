package com.njves.schoolnetwork.presenter.menu

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.request.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuPresenter(val iMenu: IMenu) {
    private val retrofit = NetworkService.instance.getRetrofit()
    private val profileService = retrofit.create(ProfileService::class.java)
    fun getProfile(uid: String){

        val getProfile = profileService.getProfile("GET", uid)
        getProfile.enqueue(object : Callback<NetworkResponse<Profile>>{
            override fun onResponse(call: Call<NetworkResponse<Profile>>, response: Response<NetworkResponse<Profile>>) {
                val code = response.body()?.code
                val message  = response.body()?.message!!
                val profile = response.body()?.data!!
                if(response.code()!=200) iMenu.onError(response.errorBody()?.string()!!)
                if(code==NetworkResponse.SUCCESS_RESPONSE) {
                    iMenu.onSuccess(profile)
                }else{
                    iMenu.onError(message)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
                iMenu.onFail(t)
            }
        })
    }
}