package com.njves.schoolnetwork.presenter.school

import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.presenter.IError

interface ISchool : IError{
    fun onSuccess(positionList : List<School>)
}