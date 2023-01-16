package com.myshopproject.domain.preferences

import kotlinx.coroutines.flow.Flow

interface MyPreferences {
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveUserId(id: Int)
    suspend fun saveEmailUser(email: String)
    suspend fun saveGenderUser(gender: Int)
    suspend fun saveNameUser(name: String)
    suspend fun savePhoneNumber(phone: String)
    fun getRefreshToken(): Flow<String>
    fun getAccessToken(): Flow<String>
    fun getUserId(): Flow<Int>
    fun getEmailUser(): Flow<String>
    fun getGenderUser(): Flow<Int>
    fun getNameUser(): Flow<String>
    fun getPhoneNumber(): Flow<String>
    suspend fun removeSession()
}