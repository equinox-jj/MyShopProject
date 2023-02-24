package com.myshopproject.data.di

import com.myshopproject.data.utils.interceptor.ConnectionManager
import com.myshopproject.data.utils.interceptor.ConnectionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectionModule {
    @Binds
    abstract fun bindConnectionManager(connectionManagerImpl: ConnectionManagerImpl): ConnectionManager
}