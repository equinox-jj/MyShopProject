package com.myshopproject.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ChangeImageErrorMessageDTO(
    @SerializedName("image") val image: List<String>
)