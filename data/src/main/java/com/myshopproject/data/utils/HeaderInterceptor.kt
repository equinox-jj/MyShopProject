package com.myshopproject.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
            .header("apiKey", "TuIBt77u7tZHi8n7WqUC")

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}