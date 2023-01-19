package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductResponseDTO(
    @SerializedName("success") val success: DataProductResultDTO
)