package com.myshopproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myshopproject.data.local.converter.RoomConverter
import com.myshopproject.data.local.dao.ProductCartDao
import com.myshopproject.domain.entities.DetailProductData

@Database(
    entities = [DetailProductData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverter::class)
abstract class ProductCartDatabase : RoomDatabase() {

    abstract fun productCartDao(): ProductCartDao

}