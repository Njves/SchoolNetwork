package com.njves.schoolnetwork.presenter.task.task_detail

import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.presenter.IError

interface ITaskDetail : IError{
    fun onDelete()
    fun onInit(task: TaskViewModel)

}