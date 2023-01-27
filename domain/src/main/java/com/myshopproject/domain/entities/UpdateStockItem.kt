package com.myshopproject.domain.entities

import com.google.gson.annotations.SerializedName

data class UpdateStockItem(
    val id_product: String,
    val stock: Int
)
