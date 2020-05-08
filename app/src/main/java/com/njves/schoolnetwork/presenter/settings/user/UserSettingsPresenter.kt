package com.njves.schoolnetwork.presenter.settings.user

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.request.UserService
import com.njves.schoolnetwork.preferences.AuthStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSettingsPresenter(private val iUserSettings: IUserSettings,private val navigator: UserSettingsNavigator, private val preferences: AuthStorage) {
    private var userService = NetworkService.instance.getRetrofit().create(UserService::class.java)
    companion object{
        const val EMAIL_TYPE = "CHANGE_EMAIL"
        const val PASSWORD_TYPE = "CHANGE_PASSWORD"
        const val SCHOOL_TYPE = "CHANGE_SCHOOL"
    }
    fun getUser(){
        userService.getUser("GET",preferences.getUserDetails()?:"").enqueue(object: Callback<NetworkResponse<User>>{
            override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {
                if(response.body()!!.code == NetworkResponse.SUCCESS_RESPONSE){
                    val user = response.body()!!.data
                    iUserSettings.onUserReceive(user)
                    iUserSettings.onViewFill(user.name, user.email, user.schoolNumber)
                }else{
                    iUserSettings.onError(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<User>>, t: Throwable) {
                iUserSettings.onFail(t)
            }
        })
    }

    fun showEmailDialog(email: String){
        navigator.showEmailDialog(email)
    }

    fun updateEmail(uid: String,newEmail: String){
        val map = HashMap<String, String>()
        map["type"] = EMAIL_TYPE
        map["uid"] = uid
        map["new_email"] = newEmail
        val changeEmail = userService.updateEmail(map)
        changeEmail.enqueue(object: Callback<NetworkResponse<Void>>{
            override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
                var message = response.body()?.message
                if(message==null)
                    message = response.errorBody()?.string()
                if(response.body()?.code==NetworkResponse.SUCCESS_RESPONSE){
                    iUserSettings.onEmailUpdate()
                }else{
                    iUserSettings.onError(message)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
                iUserSettings.onFail(t)
            }
        })
    }

    fun updatePassword(uid: String, oldPassword: String?, newPassword: String?, newPasswordRetry: String?){
        val map = HashMap<String, String?>()
        map["type"] = PASSWORD_TYPE
        map["uid"] = uid
        map["old_password"] = oldPassword
        map["new_password"] = newPassword
        map["new_password_retry"] = newPasswordRetry
        val changePassword = userService.updatePassword(map)
        changePassword.enqueue(object: Callback<NetworkResponse<Void>>{
            override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
                // TODO: Обработчик HTTP кодов
                if(response.code()!=200) iUserSettings.onError("Неизвестная ошибка")
                if(response.body()?.code==NetworkResponse.SUCCESS_RESPONSE){
                    iUserSettings.onPasswordUpdate()
                }else{
                    iUserSettings.onError(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
                iUserSettings.onFail(t)
            }
        })
    }

    fun updateSchool(uid: String,school: Int){
        val map = HashMap<String, String>()
        map["type"] = SCHOOL_TYPE
        map["uid"] = uid
        map["school"] = school.toString()
        val changeSchool = userService.updateEmail(map)
        changeSchool.enqueue(object: Callback<NetworkResponse<Void>>{
            override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
                if(response.body()?.code==NetworkResponse.SUCCESS_RESPONSE){
                    iUserSettings.onSchoolUpdate()
                }else{
                    iUserSettings.onError(response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
                iUserSettings.onFail(t)
            }
        })
    }

    fun showPasswordDialog(password: String) {
        navigator.showPasswordDialog(password)
    }

    fun showSchoolDialog(s: Int) {
        navigator.showSchoolDialog(s)
    }


}