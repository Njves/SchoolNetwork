package com.njves.schoolnetwork.presenter.task.task_detail

import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel

interface ITaskDetail {
    fun onDelete(task: TaskViewModel)
    fun onInit(task: TaskViewModel)

}