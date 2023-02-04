package com.myshopproject.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangeImageResultDTO(
    @SerializedName("path") val path: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
