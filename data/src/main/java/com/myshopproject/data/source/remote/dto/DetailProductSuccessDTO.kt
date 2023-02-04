package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductSuccessDTO(
    @SerializedName("data") val data: com.myshopproject.data.source.remote.dto.DetailProductDataDTO,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)