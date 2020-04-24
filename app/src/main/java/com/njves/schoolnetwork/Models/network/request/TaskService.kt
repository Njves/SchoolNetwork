package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.TASK_DELETE
import com.njves.schoolnetwork.Models.network.TASK_GET
import com.njves.schoolnetwork.Models.network.TASK_POST
import com.njves.schoolnetwork.Models.network.TASK_UPDATE
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.Task
import retrofit2.Call
import retrofit2.http.*

interface TaskService {
    @POST(TASK_POST)
    fun postCallTask(@Body body : RequestTaskModel): Call<NetworkResponse<Task>>

    @GET(TASK_GET)
    fun getTaskList(@Query("type") type : String,@Query("uid") uid: String): Call<NetworkResponse<List<Task>>>
    // TODO: Исправить костыль
    @GET("api/task/")
    fun getMyTaskList(@Query("type") type : String, @Query("uid") uid : String) : Call<NetworkResponse<List<Task>>>

    @POST(TASK_UPDATE)
    fun updateTask(@Body body : RequestTaskModel) : Call<NetworkResponse<Task>>

    @POST(TASK_DELETE)
    @FormUrlEncoded
    fun deleteTask(@Field("type") type : String,@Field("task_uid") taskUid : String, @Field("uid") uid : String ) : Call<NetworkResponse<Void>>

}