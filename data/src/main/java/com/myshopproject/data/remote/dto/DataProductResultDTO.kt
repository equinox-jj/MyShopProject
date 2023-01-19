package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductResultDTO(
    @SerializedName("data") val data: List<DataProductDTO>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)