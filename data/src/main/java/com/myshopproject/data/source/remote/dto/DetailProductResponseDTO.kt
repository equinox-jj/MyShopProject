package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductResponseDTO(
    @SerializedName("success") val success: DetailProductSuccessDTO
)