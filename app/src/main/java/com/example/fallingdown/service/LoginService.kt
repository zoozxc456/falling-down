package com.example.fallingdown.service

import com.example.fallingdown.model.LoginRequestModel
import com.example.fallingdown.model.LoginResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface ILoginService{
    @POST("User/Login")
    fun login(@Body model : LoginRequestModel):Call<LoginResponseModel>
}

private const val BASE_URL ="http://10.0.2.2:5162/api/"

object LoginServiceApi{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService:ILoginService= retrofit.create(ILoginService::class.java)
}