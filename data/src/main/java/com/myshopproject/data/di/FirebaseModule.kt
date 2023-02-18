package com.myshopproject.data.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.myshopproject.data.repository.FirebaseRepositoryImpl
import com.myshopproject.domain.repository.FirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun providesFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseRepository(
        fcm: FirebaseRemoteConfig
    ): FirebaseRepository = FirebaseRepositoryImpl(fcm)
}