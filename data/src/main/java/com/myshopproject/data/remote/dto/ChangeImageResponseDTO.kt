package com.myshopproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangeImageResponseDTO(
    @SerializedName("success") val success: ChangeImageResultDTO
)
