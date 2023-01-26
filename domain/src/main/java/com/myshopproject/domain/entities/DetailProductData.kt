package com.myshopproject.domain.entities

data class DetailProductData(
    val id: Int,
    val date: String,
    val desc: String,
    val harga: String,
    val image: String,
    val imageProduct: List<DetailProductImage>,
    val nameProduct: String,
    val rate: Int,
    val size: String,
    val stock: Int,
    val type: String,
    val weight: String,
    val isFavorite: Boolean
)
