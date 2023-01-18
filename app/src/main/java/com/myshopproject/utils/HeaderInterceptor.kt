package com.myshopproject.utils

import com.myshopproject.domain.preferences.MyPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val pref: MyPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking(Dispatchers.IO) {
            pref.getAccessToken().first()
        }

        val request = chain.request()
        val requestBuilder = request.newBuilder()
            .header("apiKey", "TuIBt77u7tZHi8n7WqUC")
            .addHeader("Authorization", accessToken)

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}