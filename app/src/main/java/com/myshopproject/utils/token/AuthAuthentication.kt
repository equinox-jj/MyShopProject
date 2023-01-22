package com.myshopproject.utils.token

import com.myshopproject.data.remote.dto.RefreshTokenResponseDTO
import com.myshopproject.data.remote.network.ApiRefreshToken
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
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val authRefresh = pref.getAuthRefresh().first()

            val newToken = getNewToken(
                authRefresh.userId,
                authRefresh.accessToken,
                authRefresh.refreshToken
            )

            if (!newToken.isSuccessful || newToken.body() == null || newToken.code() == 401) {
                pref.clearSession() // Clear Preferences
            }

            newToken.body()?.let {
                pref.saveAccessToken(it.success.accessToken)
                pref.saveRefreshToken(it.success.refreshToken)
                response.request.newBuilder()
                    .header("Authorization", it.success.accessToken) // Save New Token To Header
                    .build()
            }
        }
    }

    private suspend fun getNewToken(
        userId: Int?,
        accessToken: String?,
        refreshToken: String?
    ): retrofit2.Response<RefreshTokenResponseDTO> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.17.20.201/training_android/public/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val services = retrofit.create(ApiRefreshToken::class.java)
        return services.getRefreshToken(userId, accessToken, refreshToken)
    }
}