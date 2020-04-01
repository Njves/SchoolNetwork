package com.njves.schoolnetwork.Models

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDeleteController : Callback<NetworkResponse<Void>> {

    override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
        println("Fail request to delete task $t")
    }

    override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
        response.body()?.data

    }

}