package com.njves.schoolnetwork.presenter.task.task_edit

import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.presenter.IError

interface ITaskEdit : IError{
    fun onSuccessGetList(list: List<Profile>)
    fun pickDate()
    fun onSuccessSend()

}