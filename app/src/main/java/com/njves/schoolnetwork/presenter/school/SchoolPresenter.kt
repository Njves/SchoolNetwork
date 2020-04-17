package com.njves.schoolnetwork.presenter.school

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.Models.network.request.SchoolListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchoolPresenter(val iSchool: ISchool) {
    fun getSchool(){
        val schoolService = NetworkService.instance.getRetrofit().create(SchoolListService::class.java)
        val callList = schoolService.callSchoolList()
        callList.enqueue(object : Callback<List<School>>{
            override fun onResponse(call: Call<List<School>>, response: Response<List<School>>) {
                val list = response.body()
                if(list!=null){
                    iSchool.onSuccess(list)
                }else{
                    iSchool.onError("Не удалось получить список школ")
                }
            }

            override fun onFailure(call: Call<List<School>>, t: Throwable) {
                iSchool.onFail(t)
            }
        })
    }
}