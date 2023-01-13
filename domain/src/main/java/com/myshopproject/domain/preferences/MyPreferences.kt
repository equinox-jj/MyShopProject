package com.myshopproject.domain.preferences

import com.myshopproject.domain.entities.UserDataPref
import kotlinx.coroutines.flow.Flow

interface MyPreferences {
    suspend fun saveSession(
        refreshToken: String,
        accessToken: String,
        id: Int,
        email: String,
        gender: Int,
        name: String,
        phone: String
    )
    suspend fun saveUserId(userIds: Int)
    suspend fun removeSession()
    fun getSession(): Flow<UserDataPref>
}