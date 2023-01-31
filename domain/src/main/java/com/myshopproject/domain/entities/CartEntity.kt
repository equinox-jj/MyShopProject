package com.myshopproject.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myshopproject.domain.utils.Constants.CART_TABLE

@Entity(CART_TABLE)
data class CartEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "product_name_cart")
    val productName: String = "",
    @ColumnInfo(name = "price_cart")
    val price: String = "",
    @ColumnInfo(name = "image_cart")
    val image: String = "",
    @ColumnInfo(name = "quantity_cart")
    val quantity: Int = 0,
    @ColumnInfo(name = "total_price_cart")
    val totalPriceCart: Int = 0,
    @ColumnInfo(name = "is_checked_cart")
    val isChecked: Boolean = false,
    @ColumnInfo(name = "stock_product_cart")
    val stock_product: Int = 0
)
