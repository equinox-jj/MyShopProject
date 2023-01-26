package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductResultDTO(
    @SerializedName("data") val data: List<com.myshopproject.data.source.remote.dto.DataProductDTO>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)