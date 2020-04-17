package com.njves.schoolnetwork.presenter.task.task_detail

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailPresenter(iTaskDetail: ITaskDetail) {
    private var retrofit = NetworkService.instance.getRetrofit()
    private var taskService = retrofit.create(TaskService::class.java)
    fun deleteTask(uid: String,task: TaskViewModel){
        val call = taskService.deleteTask("DELETE",task.uid,uid)
        call.enqueue(object : Callback<NetworkResponse<Void>> {
            override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
                TODO("Not yet implemented")
            }
            })
    }

}

}