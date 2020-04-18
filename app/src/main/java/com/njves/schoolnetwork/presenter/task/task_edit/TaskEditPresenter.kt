package com.njves.schoolnetwork.presenter.task.task_edit

import android.util.Log
import android.widget.Toast
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.TaskPostModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.Models.network.request.TeachersService
import com.njves.schoolnetwork.fragments.TaskEditFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskEditPresenter(val iTaskEdit: ITaskEdit) {
    private var retrofit = NetworkService.instance.getRetrofit()
    private var taskService  = retrofit.create(TaskService::class.java)
    private var teachersService = retrofit.create(TeachersService::class.java)
    fun sendTask(task: TaskPostModel){
        iTaskEdit.showProgressBar()
        val call = taskService.postCallTask(RequestTaskModel("POST", task))
        call.enqueue(object : Callback<NetworkResponse<TaskPostModel>>{
            override fun onResponse(call: Call<NetworkResponse<TaskPostModel>>, response: Response<NetworkResponse<TaskPostModel>>) {
                iTaskEdit.hideProgressBar()
                val code = response.body()?.code
                val message = response.body()?.message
                val body = response.body()
                if(response.code()!=200) iTaskEdit.onError(response.errorBody()?.string()!!)
                if(code==NetworkResponse.SUCCESS_RESPONSE)iTaskEdit.onSuccessSend()
                else iTaskEdit.onError(message!!)
            }

            override fun onFailure(call: Call<NetworkResponse<TaskPostModel>>, t: Throwable) {
                iTaskEdit.hideProgressBar()
                iTaskEdit.onFail(t)
            }
        })
    }
    fun getListProfile(pos: Int, schoolNum: Int){
        iTaskEdit.showProgressBar()
        val call = teachersService.getTeacherList(pos, schoolNum)
        call.enqueue(object : Callback<NetworkResponse<List<Profile>>>{
            override fun onResponse(
                call: Call<NetworkResponse<List<Profile>>>,
                response: Response<NetworkResponse<List<Profile>>>) {
                iTaskEdit.hideProgressBar()
                iTaskEdit.onSuccessGetList(response.body()?.data!!)

            }
            override fun onFailure(call: Call<NetworkResponse<List<Profile>>>, t: Throwable) {
                iTaskEdit.hideProgressBar()
                iTaskEdit.onFail(t)
            }
        })


    }
}