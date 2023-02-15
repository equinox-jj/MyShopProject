package com.myshopproject.di

import android.content.Context
import com.myshopproject.data.BuildConfig
import com.myshopproject.data.preferences.MyPreferencesImpl
import com.myshopproject.data.repository.AuthRepositoryImpl
import com.myshopproject.data.repository.ProductRepositoryImpl
import com.myshopproject.data.source.local.dao.FirebaseMessageDao
import com.myshopproject.data.source.local.dao.ProductCartDao
import com.myshopproject.data.source.remote.network.ApiAuth
import com.myshopproject.data.source.remote.network.ApiProduct
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.repository.FirebaseRepository
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.usecase.LocalInteractor
import com.myshopproject.domain.usecase.LocalUseCase
import com.myshopproject.domain.usecase.RemoteInteractor
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.utils.Constants.BASE_URL
import com.myshopproject.utils.token.AuthAuthentication
import com.myshopproject.utils.token.AuthExpiredToken
import com.myshopproject.utils.token.HeaderInterceptor
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
object AppModule {

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
    fun providesAuthInterceptor(
        @ApplicationContext context: Context,
        pref: MyPreferences
    ): AuthExpiredToken = AuthExpiredToken(context, pref)

    @Singleton
    @Provides
    fun providesHeaderInterceptor(pref: MyPreferences): HeaderInterceptor = HeaderInterceptor(pref)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        authExpiredToken: AuthExpiredToken,
        authAuthentication: AuthAuthentication,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(authExpiredToken)
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
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiAuth(retrofit: Retrofit): ApiAuth {
        return retrofit.create(ApiAuth::class.java)
    }

    @Singleton
    @Provides
    fun provideApiProduct(retrofit: Retrofit): ApiProduct {
        return retrofit.create(ApiProduct::class.java)
    }

    @Singleton
    @Provides
    fun providesAuthRepository(apiAuth: ApiAuth): AuthRepository {
        return AuthRepositoryImpl(apiAuth)
    }

    @Singleton
    @Provides
    fun providesProductRepository(
        apiProduct: ApiProduct,
        cartDao: ProductCartDao,
        fcmDao: FirebaseMessageDao
    ): ProductRepository {
        return ProductRepositoryImpl(apiProduct, cartDao, fcmDao)
    }

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): MyPreferences {
        return MyPreferencesImpl(context)
    }

    @Provides
    @Singleton
    fun providesRemoteUseCase(
        authRepository: AuthRepository,
        productRepository: ProductRepository,
        firebaseRepository: FirebaseRepository
    ): RemoteUseCase {
        return RemoteInteractor(authRepository, productRepository, firebaseRepository)
    }

    @Provides
    @Singleton
    fun providesLocalUseCase(
        productRepository: ProductRepository
    ): LocalUseCase {
        return LocalInteractor(productRepository)
    }
}