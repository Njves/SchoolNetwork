package com.njves.schoolnetwork.presenter.task.task_list

import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.presenter.IError

interface TaskListContract : IError {
    fun onRefresh()
    fun onNavigateToDetail(task: Task)
    fun onResponseList(taskList: List<Task>)
    fun onResponseEmptyList()
    fun showConfirmDialog()


}