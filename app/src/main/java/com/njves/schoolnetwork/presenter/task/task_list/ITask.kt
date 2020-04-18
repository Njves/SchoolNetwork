package com.njves.schoolnetwork.presenter.task.task_list

import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.presenter.IError

interface ITask : IError {
    fun onRefresh()
    fun onItemClickListener(task: TaskViewModel)
    fun onSuccessGet(taskList: List<TaskViewModel>)
    fun onSuccessGetEmptyList()
    fun showConfirmDialog()


}