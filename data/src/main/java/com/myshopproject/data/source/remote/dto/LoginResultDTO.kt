package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class LoginResultDTO(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("data_user") val dataUser: com.myshopproject.data.source.remote.dto.DataUserDTO,
    @SerializedName("message") val message: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("status") val status: Int
)