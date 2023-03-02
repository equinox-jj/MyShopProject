package com.myshopproject.data.utils

import com.myshopproject.data.source.remote.dto.*
import org.mockito.ArgumentMatchers

fun dummyDetailProductResponse(): DetailProductResponseDTO {
    val dataImageDetail = DetailProductImageDTO(
        imageProduct = "",
        titleProduct = ""
    )
    val detailProductData = DetailProductDataDTO(
        date = "",
        desc = "",
        harga = "",
        id = 0,
        image = "",
        imageProduct = listOf(dataImageDetail),
        nameProduct = "",
        rate = 0,
        size = "",
        stock = 0,
        type = "",
        weight = "",
        isFavorite = ArgumentMatchers.anyBoolean()
    )


    return DetailProductResponseDTO(
        success = DetailProductSuccessDTO(
            data = detailProductData,
            message = "",
            status = null
        )
    )
}

fun dummyProductResponse(): DataProductResponseDTO {
    val dataItemFavorite = DataProductDTO(
        id = 0,
        date = "",
        image = "",
        nameProduct = "",
        harga = "",
        size = "",
        rate = 0,
        weight = "",
        stock = 0,
        type = "",
        desc = ""
    )

    return DataProductResponseDTO(
        success = DataProductResultDTO(
            data = listOf(dataItemFavorite),
            message = "",
            status = null
        )
    )
}

fun dummySuccessResponse(): SuccessResponseDTO {
    return SuccessResponseDTO(
        success = SuccessResultDTO(
            message = "",
            status = null
        )
    )
}

fun dummyLoginResponse():LoginResponseDTO {
    val dataUser = DataUserDTO(
        email = "",
        gender = 0,
        id = 0,
        image = "",
        name = "",
        path = "",
        phone = ""
    )

    return LoginResponseDTO(
        success = LoginResultDTO(
            accessToken = "",
            dataUser = dataUser,
            message = "",
            refreshToken = "",
            status = null
        )
    )
}

fun dummyChangeImageResponse(): ChangeImageResponseDTO {
    val changeImageResult = ChangeImageResultDTO(
        path = "",
        message = "",
        status = null
    )

    return ChangeImageResponseDTO(
        success = changeImageResult
    )
}