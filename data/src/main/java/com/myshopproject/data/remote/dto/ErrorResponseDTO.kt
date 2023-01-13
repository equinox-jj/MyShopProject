package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ErrorResponseDTO(
    @SerializedName("error") val error: ErrorResultDTO
)