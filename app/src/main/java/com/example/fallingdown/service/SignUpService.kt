package com.example.fallingdown.service

import com.example.fallingdown.model.SignUpRequestModel
import com.example.fallingdown.model.SignUpResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ISignUpService{
    @POST("User")
    fun signup(@Body model : SignUpRequestModel): Call<SignUpResponseModel>
}

private const val BASE_URL ="http://10.0.2.2:5162/api/"

object SignUpService{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService:ISignUpService= retrofit.create(ISignUpService::class.java)
}