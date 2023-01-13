package com.myshopproject.domain.entities

data class UserDataPref(
    val refreshToken: String,
    val accessToken: String,
    val id: Int,
    val email: String,
    val gender: Int,
    val name: String,
    val phone: String
)