package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class RefreshTokenResultDTO(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("message") val message: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("status") val status: Int
)