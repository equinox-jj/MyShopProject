package com.myshopproject.domain.entities

data class DataUserResponse(
    val email: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val phone: String,
    val image: String,
    val path: String
)
