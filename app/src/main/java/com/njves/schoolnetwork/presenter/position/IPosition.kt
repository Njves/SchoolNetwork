package com.njves.schoolnetwork.presenter.position

import com.njves.schoolnetwork.Models.network.models.auth.Position
import com.njves.schoolnetwork.presenter.IError

interface IPosition : IError {
    fun onSuccess(positionList : List<Position>)
}