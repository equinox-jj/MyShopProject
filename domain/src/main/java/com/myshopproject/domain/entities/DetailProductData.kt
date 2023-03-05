package com.myshopproject.domain.entities

data class DetailProductData(
    val id: Int? = null,
    val date: String? = null,
    val desc: String? = null,
    val harga: String? = null,
    val image: String? = null,
    val imageProduct: List<DetailProductImage>,
    val nameProduct: String? = null,
    val rate: Int? = null,
    val size: String? = null,
    val stock: Int? = null,
    val type: String? = null,
    val weight: String? = null,
    val isFavorite: Boolean = false
)
