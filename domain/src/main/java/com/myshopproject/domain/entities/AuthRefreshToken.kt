package com.myshopproject.domain.entities

data class AuthRefreshToken(
    val userId: Int? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
