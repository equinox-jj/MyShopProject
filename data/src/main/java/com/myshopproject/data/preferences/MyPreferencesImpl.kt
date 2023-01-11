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
        val userToken = stringPreferencesKey("user_token")
        val userId = stringPreferencesKey("user_id")
        val userEmail = stringPreferencesKey("user_email")
        val userGender = intPreferencesKey("user_gender")
        val userName = stringPreferencesKey("user_name")
        val userPhone = stringPreferencesKey("user_phone")
    }

    private val dataStore = context.dataStore

    override suspend fun saveSession(userTokenId: String) {
        dataStore.edit { mutablePref ->
            mutablePref[userToken] = userTokenId
        }
    }

    override suspend fun removeSession() {
        dataStore.edit { mutablePref ->
            mutablePref.remove(userToken)
            mutablePref.clear()
        }
    }

    override fun getSession(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[userToken] ?: ""
            }
    }

    data class UserData(
        val accessToken: String,
        val id: String,
        val email: String,
        val gender: Int,
        val name: String,
        val phone: String
    )
}