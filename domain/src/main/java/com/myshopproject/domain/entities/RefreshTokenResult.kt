package com.myshopproject.domain.entities

data class RefreshTokenResult(
    val accessToken: String,
    val message: String,
    val refreshToken: String,
    val status: Int
)