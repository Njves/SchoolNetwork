package com.njves.schoolnetwork.presenter.task.task_detail

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailPresenter(val iTaskDetail: ITaskDetail) {
    private var retrofit = NetworkService.instance.getRetrofit()
    private var taskService = retrofit.create(TaskService::class.java)
    fun deleteTask(uid: String,task: TaskViewModel){
        iTaskDetail.showProgressBar()
        val call = taskService.deleteTask("DELETE",task.uid,uid)
        call.enqueue(object : Callback<NetworkResponse<Void>> {
            override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
                iTaskDetail.hideProgressBar()
                if(response.code()!=200) iTaskDetail.onError(response.errorBody()?.string()!!)
                val body = response.body()
                if(body?.code==NetworkResponse.SUCCESS_RESPONSE){
                    iTaskDetail.onDelete()
                }
            }

            override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
                iTaskDetail.hideProgressBar()
                iTaskDetail.onFail(t)
            }
        })
    }
    fun initTask(task : TaskViewModel){
        iTaskDetail.onInit(task)
    }
}

