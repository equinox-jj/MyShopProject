package com.myshopproject.data.utils

import com.myshopproject.domain.preferences.MyPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val pref: MyPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            pref.getAccessToken().first()
        }

        val request = chain.request().newBuilder()
        request.addHeader("Authorization", accessToken)
        return chain.proceed(request.build())
    }
}