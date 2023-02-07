package com.myshopproject.domain.entities

data class UpdateStockProduct(
    val id_user: String,
    val data_stock: List<UpdateStockItem>
)
