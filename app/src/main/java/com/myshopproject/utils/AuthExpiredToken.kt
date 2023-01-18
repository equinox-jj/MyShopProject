package com.myshopproject.utils

import android.content.Context
import android.content.Intent
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.presentation.login.LoginActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthExpiredToken @Inject constructor(
    private val context: Context,
    private val pref: MyPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            runBlocking {
                pref.clearSession() // Clear Preferences
            }
            val intent = Intent(context, LoginActivity::class.java) // Back to Login Screen
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return response
        }
        return response
    }
}