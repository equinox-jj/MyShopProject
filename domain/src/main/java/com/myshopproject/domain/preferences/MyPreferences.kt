package com.myshopproject.domain.preferences

import com.myshopproject.domain.entities.AuthRefreshToken
import kotlinx.coroutines.flow.Flow

interface MyPreferences {
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveUserId(id: Int)
    suspend fun saveEmailUser(email: String)
    suspend fun saveGenderUser(gender: Int)
    suspend fun saveNameUser(name: String)
    suspend fun savePhoneNumber(phone: String)
    suspend fun saveImageUser(image: String)
    suspend fun saveLanguage(language: Int)
    suspend fun saveAuthRefresh(userId: Int, accessToken: String, refreshToken: String)
    fun getRefreshToken(): Flow<String>
    fun getAccessToken(): Flow<String>
    fun getUserId(): Flow<Int>
    fun getEmailUser(): Flow<String>
    fun getGenderUser(): Flow<Int>
    fun getNameUser(): Flow<String>
    fun getPhoneNumber(): Flow<String>
    fun getImageUser(): Flow<String>
    fun getLanguage(): Flow<Int>
    fun getAuthRefresh(): Flow<AuthRefreshToken>
    suspend fun clearSession()
}