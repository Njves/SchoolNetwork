package com.njves.schoolnetwork.Models.network.request

import com.njves.schoolnetwork.Models.network.models.auth.Position
import retrofit2.Call
import retrofit2.http.GET

interface PositionListService {
    @GET("API/DB/positions.php")
    fun callPositionList() : Call<List<Position>>
}