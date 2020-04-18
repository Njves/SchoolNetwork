package com.njves.schoolnetwork.presenter.task

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.adapter.TaskAdapter
import com.njves.schoolnetwork.callback.OnRecyclerViewTaskOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.NullPointerException

class TaskPresenter(private val iTask: ITask) : SwipeRefreshLayout.OnRefreshListener, TaskAdapter.TaskAdapterActionListener{
    private var retrofit = NetworkService.instance.getRetrofit()
    private var taskService = retrofit.create(TaskService::class.java)
    override fun onRefresh() {
        iTask.onRefresh()
    }

    override fun onItemClick(item: TaskViewModel) {
        iTask.onItemClickListener(item)
    }

    fun getTaskList(type: String, uid: String){
        iTask.showProgressBar()
        val call = taskService.getTaskList(type, uid)
        call.enqueue(object : Callback<NetworkResponse<List<TaskViewModel>>> {
            override fun onResponse(call: Call<NetworkResponse<List<TaskViewModel>>>,
                                    response: Response<NetworkResponse<List<TaskViewModel>>>) {
                iTask.hideProgressBar()
                val code = response.body()?.code
                val message = response.body()?.message
                val taskList = response.body()?.data

                if(response.code()!=200) {iTask.onError(response.errorBody()?.string()!!); return}


                if(code==NetworkResponse.SUCCESS_RESPONSE) {
                    if(taskList != null){
                        if(taskList.isNotEmpty()){
                            iTask.onSuccessGet(taskList)
                        }else{
                            iTask.onSuccessGetEmptyList()
                        }
                    }else{
                        iTask.onError("Ошибка получения списка задач")
                    }

                }
                else{
                    iTask.onError(message!!)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<List<TaskViewModel>>>, t: Throwable) {
                iTask.hideProgressBar()
                iTask.onFail(t)
            }
        })
    }
}