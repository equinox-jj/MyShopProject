package com.myshopproject.domain.preferences

import kotlinx.coroutines.flow.Flow

interface MyPreferences {
    suspend fun saveSession(userTokenId: String)
    suspend fun removeSession()
    fun getSession(): Flow<String>
}