package com.myshopproject.data.utils

import com.myshopproject.data.remote.dto.RefreshTokenResponseDTO
import com.myshopproject.data.remote.network.ApiService
import com.myshopproject.domain.preferences.MyPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthentication @Inject constructor(
    private val pref: MyPreferences
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val accessToken = runBlocking {
            pref.getAccessToken().first()
        }
        val refreshToken = runBlocking {
            pref.getRefreshToken().first()
        }
        val userId = runBlocking {
            pref.getUserId().first()
        }

        return runBlocking {
            val newToken = getNewToken(userId, accessToken, refreshToken)

            if (!newToken.isSuccessful || newToken.body() == null) {
                pref.removeSession()
            }

            newToken.body()?.let {
                pref.saveAccessToken(it.success.accessToken)
                response.request.newBuilder()
                    .header("Authorization", it.success.accessToken)
                    .build()
            }
        }
    }

    private suspend fun getNewToken(userId: Int?, accessToken: String?, refreshToken: String?): retrofit2.Response<RefreshTokenResponseDTO> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient =  OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.17.20.201/training_android/public/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val services = retrofit.create(ApiService::class.java)
        return services.getRefreshToken(userId, accessToken, refreshToken)
    }
}