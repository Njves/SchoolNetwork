package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.Task
import retrofit2.Call
import retrofit2.http.*

interface TaskService {
    @POST("API/Task/index.php")
    fun postCallTask(@Body body : RequestTaskModel): Call<NetworkResponse<Task>>

    @GET("API/Task")
    fun getTaskList(@Query("type") type : String,@Query("uid") uid: String): Call<NetworkResponse<List<Task>>>

    @POST("API/Task")
    fun updateTask(@Body body : RequestTaskModel) : Call<NetworkResponse<Task>>


}