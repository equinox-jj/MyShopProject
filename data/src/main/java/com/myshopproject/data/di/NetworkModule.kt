package com.myshopproject.data.di

import android.content.Context
import com.myshopproject.data.BuildConfig
import com.myshopproject.data.preferences.MyPreferencesImpl
import com.myshopproject.data.remote.network.ApiService
import com.myshopproject.data.repository.AuthRepositoryImpl
import com.myshopproject.data.utils.AuthAuthentication
import com.myshopproject.data.utils.AuthInterceptor
import com.myshopproject.data.utils.HeaderInterceptor
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

    @Singleton
    @Provides
    fun providesAuthAuthentication(pref: MyPreferences): AuthAuthentication = AuthAuthentication(pref)

    @Singleton
    @Provides
    fun providesAuthInterceptor(pref: MyPreferences): AuthInterceptor = AuthInterceptor(pref)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
//        headerInterceptor: HeaderInterceptor,
        authInterceptor: AuthInterceptor,
        authAuthentication: AuthAuthentication,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
//            .addInterceptor(headerInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(authAuthentication)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
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
            .baseUrl("http://172.17.20.201/training_android/public/")
//            .baseUrl("http://localhost:3001/training_android/public/")
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
    fun providesRepository(apiService: ApiService): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }
}