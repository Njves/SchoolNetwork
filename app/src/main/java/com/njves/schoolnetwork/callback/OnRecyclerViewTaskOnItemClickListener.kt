package com.njves.schoolnetwork.callback

import com.njves.schoolnetwork.Models.network.models.task.Task

interface OnRecyclerViewTaskOnItemClickListener {
    fun onItemClick(task : Task)
}