package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductDTO(
    @SerializedName("date") val date: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("harga") val harga: String,
    @SerializedName("image") val image: String,
    @SerializedName("name_product") val nameProduct: String,
    @SerializedName("rate") val rate: Int,
    @SerializedName("size") val size: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("type") val type: String,
    @SerializedName("weight") val weight: String
)