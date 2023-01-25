package com.myshopproject.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myshopproject.domain.entities.DetailProductImage

class RoomConverter {

    private val gson = Gson()

    @TypeConverter
    fun imageProductToString(data: MutableList<DetailProductImage>): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToImageProduct(data: String): MutableList<DetailProductImage> {
        val list = object : TypeToken<MutableList<DetailProductImage>>() {}.type
        return gson.fromJson(data, list)
    }

}