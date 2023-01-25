package com.myshopproject.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_product_entity")
data class DetailProductData(
    @PrimaryKey(autoGenerate = false)
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