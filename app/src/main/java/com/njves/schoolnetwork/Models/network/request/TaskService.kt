package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.TASK_DELETE
import com.njves.schoolnetwork.Models.network.TASK_GET
import com.njves.schoolnetwork.Models.network.TASK_POST
import com.njves.schoolnetwork.Models.network.TASK_UPDATE
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.TaskPostModel
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {
    @POST(TASK_POST)
    fun postCallTask(@Body body : RequestTaskModel): Call<NetworkResponse<TaskPostModel>>

    @GET(TASK_GET)
    fun getTaskList(@Query("type") type : String,@Query("uid") uid: String): Call<NetworkResponse<List<TaskViewModel>>>
    // TODO: Исправить костыль
    @GET("api/task/")
    fun getMyTaskList(@Query("type") type : String, @Query("uid") uid : String) : Call<NetworkResponse<List<TaskViewModel>>>

    @POST(TASK_UPDATE)
    fun updateTask(@Body body : RequestTaskModel) : Call<NetworkResponse<TaskPostModel>>

    @POST(TASK_DELETE)
    @FormUrlEncoded
    fun deleteTask(@Field("type") type : String,@Field("uid") uid : String, @Field("position") pos : Int ) : Call<NetworkResponse<Void>>

}