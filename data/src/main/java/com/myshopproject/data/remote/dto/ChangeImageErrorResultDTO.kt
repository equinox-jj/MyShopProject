package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ChangeImageErrorResultDTO(
    @SerializedName("message") val message: ChangeImageErrorMessageDTO,
    @SerializedName("status") val status: Int
)