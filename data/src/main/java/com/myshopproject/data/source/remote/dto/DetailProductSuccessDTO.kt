package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductSuccessDTO(
    @SerializedName("data") val data: DetailProductDataDTO,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Int? = null
)