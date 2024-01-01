package com.example.fallingdown.model

data class LoginRequestModel(val account: String, val password: String)

data class LoginResponseModel(val userId: String, val username: String)

data class SignUpRequestModel(val account: String,val password: String,val username: String)

data class SignUpResponseModel(val userId: String)