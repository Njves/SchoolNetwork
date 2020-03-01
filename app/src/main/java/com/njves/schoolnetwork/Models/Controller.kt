package com.njves.schoolnetwork.Models

import android.content.Context
import android.os.AsyncTask
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.Storage.AuthStorage

class TaskController(val context : Context) : AsyncTask<Void, Void, TaskViewModel>()
{
    override fun onPostExecute(result: TaskViewModel?) {

    }

    override fun doInBackground(vararg params: Void?): TaskViewModel {
        val service = NetworkService.instance.getRetrofit().create(TaskService::class.java)
        val storage = AuthStorage(context)
        val call = service.getTaskList("GET", storage.getUserDetails()!!)
        val response = call.execute()
        if(response.code() ==200)
        {
            return response.body()?.data?.get(0)!!
        }else{
            return response.body()?.data?.get(0)!!
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }
}