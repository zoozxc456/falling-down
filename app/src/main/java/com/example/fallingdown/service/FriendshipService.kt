package com.example.fallingdown.service

import com.example.fallingdown.model.FriendModel
import com.example.fallingdown.model.Statistic
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface IFriendshipService{
    @GET("Friendship/{userId}")
    fun getFriendships(@Path("userId") userId:String): Call<List<FriendModel>>
}

private const val BASE_URL ="http://10.0.2.2:5162/api/"

object FriendshipService{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService:IFriendshipService= retrofit.create(IFriendshipService::class.java)
}