package com.myshopproject.domain.entities

data class LoginResult(
    val accessToken: String,
    val dataUser: DataUserResponse,
    val message: String,
    val refreshToken: String,
    val status: Int
)