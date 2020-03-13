package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.POSITION_GET
import com.njves.schoolnetwork.Models.network.models.auth.Position
import retrofit2.Call
import retrofit2.http.GET

interface PositionListService {
    @GET(POSITION_GET)
    fun callPositionList() : Call<List<Position>>
}