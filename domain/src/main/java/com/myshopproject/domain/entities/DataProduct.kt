package com.myshopproject.domain.entities

data class DataProduct(
    val id: Int,
    val date: String,
    val desc: String,
    val harga: String,
    val image: String,
    val nameProduct: String,
    val rate: Int,
    val size: String,
    val stock: Int,
    val type: String,
    val weight: String
)