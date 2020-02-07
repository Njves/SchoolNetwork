package com.njves.schoolnetwork.Models

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkService {
    companion object{
        val instance = NetworkService()
        const val TYPE_POST = "POST"
        const val TYPE_GET = "GET"
    }
    private constructor(){

    }
    public fun getRetrofit() : Retrofit
    {

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(SERVER_URL).client(okClient).build()
        return retrofit
    }


}
const val SERVER_URL = "http://host1807525.hostland.pro/SNServer/"