package com.example.fallingdown.service

import com.example.fallingdown.model.RankModel
import com.example.fallingdown.model.RecordResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface IRankService{
    @GET("Rank/{userId}")
    fun getRecordsByUserId(@Path("userId") userId:String): Call<List<RankModel>>
}

private const val BASE_URL ="http://10.0.2.2:5162/api/"

object RankService{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService:IRankService= retrofit.create(IRankService::class.java)
}