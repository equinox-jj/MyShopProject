package com.myshopproject.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.myshopproject.domain.entities.UserDataPref
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
    }

    private val dataStore = context.dataStore

    override suspend fun saveSession(
        refreshToken: String,
        accessToken: String,
        id: Int,
        email: String,
        gender: Int,
        name: String,
        phone: String
    ) {
        dataStore.edit { mutablePref ->
            mutablePref[userRefreshToken] = refreshToken
            mutablePref[userToken] = accessToken
            mutablePref[userId] = id
            mutablePref[userEmail] = email
            mutablePref[userGender] = gender
            mutablePref[userName] = name
            mutablePref[userPhone] = phone
        }
    }

    override suspend fun saveUserId(userIds: Int) {
        dataStore.edit { mutablePref ->
            mutablePref[userId] = userIds
        }
    }

    override suspend fun removeSession() {
        dataStore.edit { mutablePref ->
            mutablePref.remove(userToken)
            mutablePref.remove(userRefreshToken)
            mutablePref.remove(userId)
            mutablePref.remove(userEmail)
            mutablePref.remove(userGender)
            mutablePref.remove(userName)
            mutablePref.remove(userPhone)
            mutablePref.clear()
        }
    }

    override fun getSession(): Flow<UserDataPref> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val userRefreshTokenPref = preferences[userRefreshToken] ?: ""
                val userTokenPref = preferences[userToken] ?: ""
                val userIdPref = preferences[userId] ?: 0
                val userEmailPref = preferences[userEmail] ?: ""
                val userGenderPref = preferences[userGender] ?: 0
                val userNamePref = preferences[userName] ?: ""
                val userPhonePref = preferences[userPhone] ?: ""
                UserDataPref(
                    userRefreshTokenPref,
                    userTokenPref,
                    userIdPref,
                    userEmailPref,
                    userGenderPref,
                    userNamePref,
                    userPhonePref
                )
            }
    }
}