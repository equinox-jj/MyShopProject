package com.myshopproject.domain.entities

data class LoginResult(
    val accessToken: String? = null,
    val dataUser: DataUserResponse,
    val message: String? = null,
    val refreshToken: String? = null,
    val status: Int? = null
)