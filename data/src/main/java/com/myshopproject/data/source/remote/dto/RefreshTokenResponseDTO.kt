package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseDTO(
    @SerializedName("success") val success: RefreshTokenResultDTO
)