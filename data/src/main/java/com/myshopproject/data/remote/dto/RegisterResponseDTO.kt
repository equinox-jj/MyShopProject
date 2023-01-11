package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RegisterResponseDTO(
    @SerializedName("success") val success: RegisterResultDTO
)