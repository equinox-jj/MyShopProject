package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class ChangeImageErrorResultDTO(
    @SerializedName("message") val message: com.myshopproject.data.source.remote.dto.ChangeImageErrorMessageDTO,
    @SerializedName("status") val status: Int
)