package com.njves.schoolnetwork.Models.network.models.task

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskCall : Callback<NetworkResponse<TaskPostModel>> {
    override fun onFailure(call: Call<NetworkResponse<TaskPostModel>>, t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onResponse(
        call: Call<NetworkResponse<TaskPostModel>>,
        response: Response<NetworkResponse<TaskPostModel>>
    ) {

    }

}