package com.myshopproject.data.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.myshopproject.data.repository.FirebaseAnalyticsRepositoryImpl
import com.myshopproject.data.repository.FirebaseRepositoryImpl
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.domain.repository.FirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun providesFirebaseAnalyticsRepository(fa: FirebaseAnalytics): FirebaseAnalyticsRepository = FirebaseAnalyticsRepositoryImpl(fa)

    @Provides
    @Singleton
    fun providesFirebaseRepository(fcm: FirebaseRemoteConfig): FirebaseRepository = FirebaseRepositoryImpl(fcm)
}