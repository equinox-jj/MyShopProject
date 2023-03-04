//package com.myshopproject.presentation.utils
//
//import com.myshopproject.domain.entities.*
//
//fun dummyDetailProductResponse(): DetailProductResponse {
//    val dataImageDetail = DetailProductImage(
//        imageProduct = "",
//        titleProduct = ""
//    )
//    val detailProductData = DetailProductData(
//        date = "",
//        desc = "",
//        harga = "",
//        id = 0,
//        image = "",
//        imageProduct = listOf(dataImageDetail),
//        nameProduct = "",
//        rate = 0,
//        size = "",
//        stock = 0,
//        type = "",
//        weight = "",
//        isFavorite = false
//    )
//
//
//    return DetailProductResponse(
//        success = DetailProductSuccess(
//            data = detailProductData,
//            message = "",
//            status = null
//        )
//    )
//}
//
//fun dummyProductResponse(): DataProductResponse {
//    val dataItemFavorite = DataProduct(
//        id = 0,
//        date = "",
//        image = "",
//        nameProduct = "",
//        harga = "",
//        size = "",
//        rate = 0,
//        weight = "",
//        stock = 0,
//        type = "",
//        desc = ""
//    )
//
//    return DataProductResponse(
//        success = DataProductResult(
//            data = listOf(dataItemFavorite),
//            message = "",
//            status = null
//        )
//    )
//}
//
//fun dummySuccessResponse(): SuccessResponseStatus {
//    return SuccessResponseStatus(
//        success = SuccessResultStatus(
//            message = "",
//            status = null
//        )
//    )
//}
//
//fun dummyLoginResponse(): LoginResponse {
//    val dataUser = DataUserResponse(
//        email = "",
//        gender = 0,
//        id = 0,
//        image = "",
//        name = "",
//        path = "",
//        phone = ""
//    )
//
//    return LoginResponse(
//        success = LoginResult(
//            accessToken = "",
//            dataUser = dataUser,
//            message = "",
//            refreshToken = "",
//            status = null
//        )
//    )
//}
//
//fun dummyChangeImageResponse(): ChangeImageResponse {
//    val changeImageResult = ChangeImageResult(
//        path = "",
//        message = "",
//        status = null
//    )
//
//    return ChangeImageResponse(
//        success = changeImageResult
//    )
//}