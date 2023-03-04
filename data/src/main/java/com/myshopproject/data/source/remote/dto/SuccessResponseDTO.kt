package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class SuccessResponseDTO(
    @SerializedName("success") val success: SuccessResultDTO
)