package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class ChangeImageErrorResponseDTO(
    @SerializedName("error") val error: com.myshopproject.data.source.remote.dto.ChangeImageErrorResultDTO
)