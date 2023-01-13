package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SuccessResponseDTO(
    @SerializedName("success") val success: SuccessResultDTO
)