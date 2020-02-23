package com.njves.schoolnetwork.callback

import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel

interface OnRecyclerViewTaskOnItemClickListener {
    fun onItemClick(task : TaskViewModel)
}