package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DetailProductDataDTO(
    @SerializedName("date") val date: String? = null,
    @SerializedName("desc") val desc: String? = null,
    @SerializedName("harga") val harga: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("image_product") val imageProduct: List<DetailProductImageDTO>,
    @SerializedName("name_product") val nameProduct: String? = null,
    @SerializedName("rate") val rate: Int? = null,
    @SerializedName("size") val size: String? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("weight") val weight: String? = null,
    @SerializedName("isFavorite") val isFavorite: Boolean = false
)