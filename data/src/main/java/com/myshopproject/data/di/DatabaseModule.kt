package com.myshopproject.data.di

import android.content.Context
import androidx.room.Room
import com.myshopproject.data.source.local.ProductCartDatabase
import com.myshopproject.domain.utils.Constants.CART_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ProductCartDatabase::class.java,
        CART_DATABASE
    ).build()

    @Singleton
    @Provides
    fun providesCartDao(db: ProductCartDatabase) = db.productCartDao()
}