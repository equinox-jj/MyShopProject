package com.myshopproject.data.utils.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NoConnectionInterceptor @Inject constructor(
    private val connectionManager: ConnectionManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected()) {
            chain.proceed(chain.request())
        } else {
            throw IOException("Check your connectivity.")
        }
    }
}