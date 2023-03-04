package com.myshopproject.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myshopproject.data.source.local.dao.FirebaseMessageDao
import com.myshopproject.data.source.local.dao.ProductCartDao
import com.myshopproject.data.source.local.entity.CartEntity
import com.myshopproject.data.source.local.entity.FcmEntity

@Database(
    entities = [CartEntity::class, FcmEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductCartDatabase : RoomDatabase() {

    abstract fun productCartDao(): ProductCartDao
    abstract fun firebaseMessageDao(): FirebaseMessageDao

}