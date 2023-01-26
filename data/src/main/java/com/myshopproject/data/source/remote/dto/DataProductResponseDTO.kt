package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductResponseDTO(
    @SerializedName("success") val success: com.myshopproject.data.source.remote.dto.DataProductResultDTO
)