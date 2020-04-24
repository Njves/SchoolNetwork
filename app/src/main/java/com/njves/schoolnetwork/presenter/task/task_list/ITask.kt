package com.njves.schoolnetwork.presenter.task.task_list

import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.presenter.IError

interface ITask : IError {
    fun onRefresh()
    fun onItemClickListener(task: Task)
    fun onSuccessGet(taskList: List<Task>)
    fun onSuccessGetEmptyList()
    fun showConfirmDialog()


}