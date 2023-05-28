package com.example.fallingdown.service

import com.example.fallingdown.model.RecordResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface IRecordService{
    @GET("Record/{userId}")
    fun getRecordsByUserId(@Path("userId") userId:String): Call<RecordResponseModel>
}

private const val BASE_URL ="http://10.0.2.2:5162/api/"

object RecordService{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService:IRecordService= retrofit.create(IRecordService::class.java)
}