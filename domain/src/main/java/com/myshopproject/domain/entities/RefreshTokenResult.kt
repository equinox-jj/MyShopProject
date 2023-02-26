package com.myshopproject.domain.entities

data class RefreshTokenResult(
    val accessToken: String? = null,
    val message: String? = null,
    val refreshToken: String? = null,
    val status: Int? = null
)