package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class ErrorResponseDTO(
    @SerializedName("error") val error: com.myshopproject.data.source.remote.dto.ErrorResultDTO
)