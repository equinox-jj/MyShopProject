package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class SuccessResultDTO(
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Int? = null
)