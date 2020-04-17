package com.njves.schoolnetwork.presenter.auth

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.AuthService
import com.njves.schoolnetwork.Models.network.request.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(val iAuth : IAuth) {
    companion object{

    }
    public fun login(login : String,  password : String) {
        iAuth.showProgressBar()
        val authService = NetworkService.instance.getRetrofit().create(AuthService::class.java)
        val loginCall = authService.callLogin(login, password)
        loginCall.enqueue(object : Callback<NetworkResponse<User>> {
            override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {
                iAuth.hideProgressBar()
                val code = response.body()?.code?:1
                val message = response.body()?.message
                val user = response.body()?.data
                if(code==NetworkResponse.SUCCESS_RESPONSE){
                    iAuth.onSuccess(user)
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
    public fun forgotPassword(){

    }
}