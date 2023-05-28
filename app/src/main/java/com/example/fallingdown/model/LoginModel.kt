package com.example.fallingdown.model

data class LoginRequestModel(val account: String, val password: String)

data class LoginResponseModel(val userId: String, val username: String)
