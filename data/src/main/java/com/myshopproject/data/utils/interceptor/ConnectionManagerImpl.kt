package com.myshopproject.data.utils.interceptor

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectionManager {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    override fun isConnected(): Boolean {
        return connectivityManager.activeNetwork != null
    }
}