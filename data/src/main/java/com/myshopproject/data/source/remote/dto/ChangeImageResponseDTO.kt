package com.myshopproject.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangeImageResponseDTO(
    @SerializedName("success") val success: com.myshopproject.data.source.remote.dto.ChangeImageResultDTO
)
