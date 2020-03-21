package com.njves.schoolnetwork.Models

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.Storage.AuthStorage

class TaskListController {

    public fun deleteTask(uid : String) : String?
    {
        val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)
        val call = taskService.deleteTask("DELETE", uid, 2)
        var body : NetworkResponse<Void>? = null
        body = call.execute().body()
        return body?.toString()
    }
}