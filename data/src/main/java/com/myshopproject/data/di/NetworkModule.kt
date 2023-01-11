package com.myshopproject.data.di

import android.content.Context
import com.myshopproject.data.preferences.MyPreferencesImpl
import com.myshopproject.data.remote.network.ApiService
import com.myshopproject.data.repository.CreateAccountRepositoryImpl
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.repository.CreateAccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://172.17.20.238/training_android/public/api/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): MyPreferences {
        return MyPreferencesImpl(context)
    }

    @Singleton
    @Provides
    fun providesRepository(apiService: ApiService): CreateAccountRepository {
        return CreateAccountRepositoryImpl(apiService)
    }
}