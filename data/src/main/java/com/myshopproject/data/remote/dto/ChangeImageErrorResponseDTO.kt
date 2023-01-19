package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ChangeImageErrorResponseDTO(
    @SerializedName("error") val error: ChangeImageErrorResultDTO
)