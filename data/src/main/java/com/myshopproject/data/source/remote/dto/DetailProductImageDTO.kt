package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductImageDTO(
    @SerializedName("image_product") val imageProduct: String,
    @SerializedName("title_product") val titleProduct: String
)