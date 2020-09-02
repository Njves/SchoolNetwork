package com.njves.schoolnetwork.presenter.auth

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresenter(private val iAuth : IAuth) {
    fun register(user : User){
        iAuth.showProgressBar()
        val authService =  NetworkService.instance.getRetrofit().create(AuthService::class.java)
        val callRegister = authService.callRegister(user)
        callRegister.enqueue(object : Callback<NetworkResponse<User>>{
            override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {
                iAuth.hideProgressBar()
                val code = response.body()?.code
                val message = response.body()?.message
                val user = response.body()?.data
                if(code == NetworkResponse.SUCCESS_RESPONSE){
                    if(user!=null){
                        iAuth.onSuccess(user)
                    }else{
                        iAuth.onError("Пользователь не был получен")
                    }
                }else{
                    iAuth.onError(message!!)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<User>>, t: Throwable) {
                iAuth.hideProgressBar()
                iAuth.onFail(t)
            }
        })
    }

    fun onCheckLicense(isCheck: Boolean){
        iAuth.onCheckChange(isCheck)
    }
}