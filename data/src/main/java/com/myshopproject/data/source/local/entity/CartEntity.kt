package com.myshopproject.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myshopproject.data.utils.Constants.CART_TABLE

@Entity(tableName = CART_TABLE)
data class CartEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @ColumnInfo(name = "image")
    val image: String? = null,

    @ColumnInfo(name = "name_product")
    val nameProduct: String? = null,

    @ColumnInfo(name = "quantity")
    val quantity: Int? = 1,

    @ColumnInfo(name = "price")
    val price: String? = null,

    @ColumnInfo(name = "item_total_price")
    val itemTotalPrice: Int? = null,

    @ColumnInfo(name = "stock")
    val stock: Int? = null,

    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,
)
