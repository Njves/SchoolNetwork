package com.njves.schoolnetwork.presenter.position

import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.auth.Position
import com.njves.schoolnetwork.Models.network.request.PositionListService
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PositionPresenter(val iPosition: IPosition) {
    var retrofit = NetworkService.instance.getRetrofit()
    fun getPositions(){
        val positionService = retrofit.create(PositionListService::class.java)
        val call = positionService.callPositionList()
        call.enqueue(object : Callback<List<Position>> {
            override fun onResponse(call: Call<List<Position>>, response: Response<List<Position>>) {
                val listPositions = response.body()
                if(listPositions!=null){
                    iPosition.onSuccess(listPositions)
                }else{
                    iPosition.onError("Не удалось получить список должностей")
                }
            }

            override fun onFailure(call: Call<List<Position>>, t: Throwable) {
                iPosition.onFail(t)
            }
        })
    }
}