package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("success") val success: LoginResultDTO
)