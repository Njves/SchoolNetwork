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

class TaskPresenter(private val iTask: ITask) : SwipeRefreshLayout.OnRefreshListener, TaskAdapter.TaskAdapterActionListener{
    private var retrofit = NetworkService.instance.getRetrofit()
    private var taskService = retrofit.create(TaskService::class.java)
    init{

    }
    override fun onRefresh() {
        iTask.onRefresh()
    }

    override fun onItemClick(item: Task) {
        iTask.onNavigateToDetail(item)
    }

    fun getTaskList(type: String, uid: String){
        iTask.showProgressBar()
        val call = taskService.getTaskList(type, uid)
        call.enqueue(object : Callback<NetworkResponse<List<Task>>> {
            override fun onResponse(call: Call<NetworkResponse<List<Task>>>,
                                    response: Response<NetworkResponse<List<Task>>>) {
                iTask.hideProgressBar()
                val code = response.body()?.code
                val message = response.body()?.message
                val taskList = response.body()?.data

                if(response.code()!=200) {iTask.onError(response.errorBody()?.string()!!); return}


                if(code==NetworkResponse.SUCCESS_RESPONSE) {
                    if(taskList != null){
                        if(taskList.isNotEmpty()){
                            iTask.onResponseList(taskList)
                        }else{
                            iTask.onResponseEmptyList()
                        }
                    }else{
                        iTask.onError("Ошибка получения списка задач")
                    }

                }
                else{
                    iTask.onError(message!!)
                }
            }

            override fun onFailure(call: Call<NetworkResponse<List<Task>>>, t: Throwable) {
                iTask.hideProgressBar()
                iTask.onFail(t)
            }
        })
    }
    fun navigateToTaskEdit(){

    }
}