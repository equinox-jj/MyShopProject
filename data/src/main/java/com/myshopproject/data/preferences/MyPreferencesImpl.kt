package com.myshopproject.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.myshopproject.domain.preferences.MyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_session_pref")

class MyPreferencesImpl(context: Context) : MyPreferences {

    companion object {
        val userRefreshToken = stringPreferencesKey("user_refresh_token")
        val userToken = stringPreferencesKey("user_token")
        val userId = intPreferencesKey("user_id")
        val userEmail = stringPreferencesKey("user_email")
        val userGender = intPreferencesKey("user_gender")
        val userName = stringPreferencesKey("user_name")
        val userPhone = stringPreferencesKey("user_phone")
        val userImage = stringPreferencesKey("user_image")
        val languagePref = intPreferencesKey("language_pref")
    }

    private val dataStore = context.dataStore

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userRefreshToken] = refreshToken
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userToken] = accessToken
        }
    }

    override suspend fun saveUserId(id: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[userId] = id
        }
    }

    override suspend fun saveEmailUser(email: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userEmail] = email
        }
    }

    override suspend fun saveGenderUser(gender: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[userGender] = gender
        }
    }

    override suspend fun saveNameUser(name: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userName] = name
        }
    }

    override suspend fun savePhoneNumber(phone: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userPhone] = phone
        }
    }

    override suspend fun saveImageUser(image: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userImage] = image
        }
    }

    override suspend fun saveLanguage(language: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[languagePref] = language
        }
    }

    override fun getRefreshToken(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[userRefreshToken] ?: ""
            }
    }

    override fun getAccessToken(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[userToken] ?: ""
            }
    }

    override fun getUserId(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[userId] ?: 0
            }
    }

    override fun getEmailUser(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[userEmail] ?: ""
            }
    }

    override fun getGenderUser(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[userGender] ?: 0
            }
    }

    override fun getNameUser(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[userName] ?: ""
            }
    }

    override fun getPhoneNumber(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[userPhone] ?: ""
            }
    }

    override fun getImageUser(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[userImage] ?: ""
            }
    }

    override fun getLanguage(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[languagePref] ?: 0
            }
    }

    override suspend fun clearSession() {
        dataStore.edit { mutablePref ->
            mutablePref.clear()
        }
    }
}