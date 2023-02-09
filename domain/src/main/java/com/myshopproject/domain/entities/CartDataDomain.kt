package com.myshopproject.domain.entities

data class CartDataDomain (
    val id: Int? = null,
    val image: String?,
    val nameProduct: String?,
    val quantity: Int? = 1,
    val price: String?,
    val itemTotalPrice: Int?,
    val stock: Int? = null,
    val isChecked: Boolean = false,
)

