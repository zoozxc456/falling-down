package com.example.fallingdown.service

import com.example.fallingdown.model.LoginRequestModel
import com.example.fallingdown.model.LoginResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

data class EditUsernameResponseModel(val isSuccess:String)

interface IEditUsernameService{
    @PATCH("User/{userId}/{newUsername}")
    fun edit(@Path("userId") userId:String,@Path("newUsername") newUsername:String): Call<EditUsernameResponseModel>
}

private const val BASE_URL ="http://10.0.2.2:5162/api/"

object EditUsernameService{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService:IEditUsernameService= retrofit.create(IEditUsernameService::class.java)
}