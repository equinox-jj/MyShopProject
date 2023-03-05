package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductResultDTO(
    @SerializedName("data") val data: List<DataProductDTO>,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("total_row") val totalRow: Int? = null
)