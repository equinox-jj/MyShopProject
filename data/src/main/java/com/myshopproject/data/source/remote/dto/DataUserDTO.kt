package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class DataUserDTO(
    @SerializedName("email") val email: String? = null,
    @SerializedName("gender") val gender: Int? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("path") val path: String? = null,
    @SerializedName("phone") val phone: String? = null
)