package com.myshopproject.data.mapper

import com.myshopproject.data.source.local.entity.CartEntity
import com.myshopproject.data.source.local.entity.FcmEntity
import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.domain.entities.FcmDataDomain

fun CartDataDomain.toEntity(): CartEntity = CartEntity(
    id = id,
    image = image,
    nameProduct = nameProduct,
    quantity = quantity,
    price = price,
    itemTotalPrice = itemTotalPrice,
    stock = stock
)

fun FcmDataDomain.toEntity(): FcmEntity = FcmEntity(
    id = id,
    notificationTitle = notificationTitle,
    notificationBody = notificationBody,
    notificationDate = notificationDate,
    isRead = isRead,
    isChecked = isChecked
)