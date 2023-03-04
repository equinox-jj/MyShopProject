package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DataProductDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("desc") val desc: String? = null,
    @SerializedName("harga") val harga: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("name_product") val nameProduct: String? = null,
    @SerializedName("rate") val rate: Int? = null,
    @SerializedName("size") val size: String? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("weight") val weight: String? = null
)