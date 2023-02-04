package com.myshopproject.domain.entities

data class AuthRefreshToken(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String
)
