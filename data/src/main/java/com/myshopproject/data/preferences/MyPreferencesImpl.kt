package com.myshopproject.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.myshopproject.domain.entities.AuthRefreshToken
import com.myshopproject.domain.preferences.MyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_session_pref")

class MyPreferencesImpl(context: Context) : MyPreferences {

    companion object {
        val USER_REFRESH_TOKEN = stringPreferencesKey("user_refresh_token")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_ID = intPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_GENDER = intPreferencesKey("user_gender")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_IMAGE = stringPreferencesKey("user_image")
        val LANGUAGE_PREF = intPreferencesKey("language_pref")
    }

    private val dataStore = context.dataStore

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_TOKEN] = accessToken
        }
    }

    override suspend fun saveUserId(id: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_ID] = id
        }
    }

    override suspend fun saveEmailUser(email: String) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_EMAIL] = email
        }
    }

    override suspend fun saveGenderUser(gender: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_GENDER] = gender
        }
    }

    override suspend fun saveNameUser(name: String) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_NAME] = name
        }
    }

    override suspend fun savePhoneNumber(phone: String) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_PHONE] = phone
        }
    }

    override suspend fun saveImageUser(image: String) {
        dataStore.edit { mutablePref ->
            mutablePref[USER_IMAGE] = image
        }
    }

    override suspend fun saveLanguage(language: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[LANGUAGE_PREF] = language
        }
    }

    override suspend fun saveAuthRefresh(userId: Int, accessToken: String, refreshToken: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[USER_ID] = userId
            mutablePreferences[USER_TOKEN] = accessToken
            mutablePreferences[USER_REFRESH_TOKEN] = refreshToken
        }
    }

    override fun getRefreshToken(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_REFRESH_TOKEN] ?: ""
            }
    }

    override fun getAccessToken(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_TOKEN] ?: ""
            }
    }

    override fun getUserId(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_ID] ?: 0
            }
    }

    override fun getEmailUser(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_EMAIL] ?: ""
            }
    }

    override fun getGenderUser(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_GENDER] ?: 0
            }
    }

    override fun getNameUser(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_NAME] ?: ""
            }
    }

    override fun getPhoneNumber(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_PHONE] ?: ""
            }
    }

    override fun getImageUser(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_IMAGE] ?: ""
            }
    }

    override fun getLanguage(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[LANGUAGE_PREF] ?: 0
            }
    }

    override fun getAuthRefresh(): Flow<AuthRefreshToken> {
        return dataStore.data
            .map { preferences ->
                AuthRefreshToken(
                    preferences[USER_ID] ?: 0,
                    preferences[USER_TOKEN] ?: "",
                    preferences[USER_REFRESH_TOKEN] ?: ""
                )
            }
    }

    override suspend fun clearSession() {
        dataStore.edit { mutablePref ->
            mutablePref.clear()
        }
    }
}