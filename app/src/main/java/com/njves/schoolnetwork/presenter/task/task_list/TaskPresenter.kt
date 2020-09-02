package com.njves.schoolnetwork.presenter.task.task_list

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.adapter.TaskAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskPresenter(private val taskListContract: TaskListContract) : SwipeRefreshLayout.OnRefreshListener, TaskAdapter.TaskAdapterActionListener{
    private var retrofit = NetworkService.instance.getRetrofit()
    private var taskService = retrofit.create(TaskService::class.java)
    init{

    }
    override fun onRefresh() {
        taskListContract.onRefresh()
    }

    override fun onItemClick(item: Task) {
        taskListContract.onNavigateToDetail(item)
    }

    fun getTaskList(type: String, uid: String){
        taskListContract.showProgressBar()
        val call = taskService.getTaskList(type, uid)
        call.enqueue(object : Callback<NetworkResponse<List<Task>>> {
            override fun onResponse(call: Call<NetworkResponse<List<Task>>>,
                                    response: Response<NetworkResponse<List<Task>>>) {
                taskListContract.hideProgressBar()
                val code = response.body()?.code
                val message = response.body()?.message
                val taskList = response.body()?.data

                if(response.code()!=200) {taskListContract.onError(response.errorBody()?.string()!!); return}


                if(code==NetworkResponse.SUCCESS_RESPONSE) {
                    if(taskList != null){
                        if(taskList.isNotEmpty()){
                            taskListContract.onResponseList(taskList)
                        }else{
                            taskListContract.onResponseEmptyList()
                        }
                    }else{
                        taskListContract.onError("Ошибка получения списка задач")
                    }

                }
                else{
                    taskListContract.onError(message!!)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<List<Task>>>, t: Throwable) {
                taskListContract.hideProgressBar()
                taskListContract.onFail(t)
            }
        })
    }
    fun navigateToTaskEdit(){

    }
}