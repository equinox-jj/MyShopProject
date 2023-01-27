package com.myshopproject.domain.entities

import com.google.gson.annotations.SerializedName

data class UpdateStockProduct(
    val data_stock: List<UpdateStockItem>
)
