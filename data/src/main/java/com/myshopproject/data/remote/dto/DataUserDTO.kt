package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DataUserDTO(
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: Int,
//    @SerializedName("id") val id: Int,
//    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
//    @SerializedName("path") val path: String,
    @SerializedName("phone") val phone: String
)