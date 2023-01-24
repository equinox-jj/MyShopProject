package com.myshopproject.domain.entities

data class DetailProductData(
    val date: String,
    val desc: String,
    val harga: String,
    val id: Int,
    val image: String,
    val imageProduct: List<DetailProductImage>,
    val nameProduct: String,
    val rate: Int,
    val size: String,
    val stock: Int,
    val type: String,
    val weight: String
)
