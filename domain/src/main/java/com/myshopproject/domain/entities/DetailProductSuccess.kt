package com.myshopproject.domain.entities

data class DetailProductSuccess(
    val data: DetailProductData,
    val message: String? = null,
    val status: Int? = null
)
