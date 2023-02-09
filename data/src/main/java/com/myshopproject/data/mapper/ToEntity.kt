package com.myshopproject.data.mapper

import com.myshopproject.data.source.local.entity.CartEntity
import com.myshopproject.domain.entities.CartDataDomain

fun CartDataDomain.toEntity(): CartEntity = CartEntity(
    id = id,
    image = image,
    nameProduct = nameProduct,
    quantity = quantity,
    price = price,
    itemTotalPrice = itemTotalPrice,
    stock = stock
)