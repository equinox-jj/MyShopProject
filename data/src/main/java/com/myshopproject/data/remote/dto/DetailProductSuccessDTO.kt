package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductSuccessDTO(
    @SerializedName("data") val data: DetailProductDataDTO,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)