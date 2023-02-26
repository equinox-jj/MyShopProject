package com.myshopproject.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangeImageResultDTO(
    @SerializedName("path") val path: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Int? = null
)
