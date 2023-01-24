package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductResponseDTO(
    @SerializedName("success") val success: DetailProductSuccessDTO
)