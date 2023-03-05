package com.myshopproject.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class LoginResultDTO(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("data_user") val dataUser: DataUserDTO,
    @SerializedName("message") val message: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("status") val status: Int? = null
)