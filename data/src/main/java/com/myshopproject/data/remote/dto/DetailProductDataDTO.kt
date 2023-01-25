package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductDataDTO(
    @SerializedName("date") val date: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("harga") val harga: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("image_product") val imageProduct: List<DetailProductImageDTO>,
    @SerializedName("name_product") val nameProduct: String,
    @SerializedName("rate") val rate: Int,
    @SerializedName("size") val size: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("type") val type: String,
    @SerializedName("weight") val weight: String,
    @SerializedName("isFavorite") val isFavorite: Boolean
)