package com.myshopproject.data.utils.interceptor

import com.myshopproject.data.source.remote.dto.RefreshTokenResponseDTO
import com.myshopproject.data.source.remote.network.ApiRefreshToken
import com.myshopproject.data.utils.Constants.BASE_URL
import com.myshopproject.domain.preferences.MyPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthentication @Inject constructor(
    private val pref: MyPreferences
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val token = pref.getAuthRefresh().first()

            val newToken = getNewToken(
                token.userId,
                token.accessToken,
                token.refreshToken
            )

            if (!newToken.isSuccessful || newToken.body() == null || newToken.code() == 401) {
                pref.clearSession()
            }

            newToken.body()?.let {
                val accessToken = it.success.accessToken
                val refreshToken = it.success.refreshToken
                pref.saveAccessToken(accessToken.toString())
                pref.saveRefreshToken(refreshToken.toString())
                response.request.newBuilder()
                    .header("Authorization", it.success.accessToken.toString())
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
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val services = retrofit.create(ApiRefreshToken::class.java)
        return services.getRefreshToken(userId, accessToken, refreshToken)
    }
}