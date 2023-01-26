package com.myshopproject.data.mapper

import com.myshopproject.data.source.remote.dto.*
import com.myshopproject.domain.entities.*

fun LoginResponseDTO.toDomain() = LoginResponse(
    success = success.toDomain()
)

fun LoginResultDTO.toDomain() = LoginResult(
    accessToken = accessToken,
    dataUser = dataUser.toDomain(),
    message = message,
    refreshToken = refreshToken,
    status = status
)

fun DataUserDTO.toDomain() = DataUserResponse(
    email = email,
    gender = gender,
    name = name,
    phone = phone,
    id = id,
    image = image,
    path = path
)

fun SuccessResponseDTO.toDomain() = SuccessResponseStatus(
    success = success.toDomain()
)

fun SuccessResultDTO.toDomain() = SuccessResultStatus(
    message = message,
    status = status
)

fun ErrorResponseDTO.toDomain() = ErrorResponseStatus(
    error = error.toDomain()
)

fun ErrorResultDTO.toDomain() = ErrorResultStatus(
    message = message,
    status = status
)

fun RefreshTokenResponseDTO.toDomain() = RefreshTokenResponse(
    success = success.toDomain()
)

fun RefreshTokenResultDTO.toDomain() = RefreshTokenResult(
    accessToken = accessToken,
    message = message,
    refreshToken = refreshToken,
    status = status
)

fun ChangeImageResponseDTO.toDomain() = ChangeImageResponse(
    success = success.toDomain()
)

fun ChangeImageResultDTO.toDomain() = ChangeImageResult(
    path = path,
    message = message,
    status = status
)

fun DataProductResponseDTO.toDomain() = DataProductResponse(
    success = success.toDomain()
)

fun DataProductResultDTO.toDomain() = DataProductResult(
    data = data.map { it.toDomain() },
    message = message,
    status = status
)

fun DataProductDTO.toDomain() = DataProduct(
    id = id,
    date = date,
    desc = desc,
    harga = harga,
    image = image,
    nameProduct = nameProduct,
    rate = rate,
    size = size,
    stock = stock,
    type = type,
    weight = weight
)

fun DetailProductResponseDTO.toDomain() = DetailProductResponse(
    success = success.toDomain()
)

fun DetailProductSuccessDTO.toDomain() = DetailProductSuccess(
    data = data.toDomain(),
    message = message,
    status = status
)

fun DetailProductDataDTO.toDomain() = DetailProductData(
    date = date,
    desc = desc,
    harga = harga,
    id = id,
    image = image,
    imageProduct = imageProduct.map { it.toDomain() },
    nameProduct = nameProduct,
    rate = rate,
    size = size,
    stock = stock,
    type = type,
    weight = weight,
    isFavorite = isFavorite
)

fun DetailProductImageDTO.toDomain() = DetailProductImage(
    imageProduct = imageProduct, titleProduct = titleProduct
)

//fun UpdateRateDTO.toDomain() = UpdateRate(
//    updateRate = updateRate
//)
//
//fun UpdateStockProductDTO.toDomain() = UpdateStockProduct(
//    data_stock = data_stock.map { it.toDomain() }
//)
//
//fun UpdateStockItemDTO.toDomain() = UpdateStockItem(
//    id_product = id_product,
//    stock = stock
//)