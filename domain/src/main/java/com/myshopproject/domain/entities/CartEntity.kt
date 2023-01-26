package com.myshopproject.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myshopproject.domain.utils.Constants.CART_TABLE

@Entity(CART_TABLE)
data class CartEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "product_name")
    val product_name: String = "",
    @ColumnInfo(name = "price")
    val price: String = "",
    @ColumnInfo(name = "image")
    val image: String = "",
    @ColumnInfo(name = "quantity")
    val quantity: Int = 0
)
