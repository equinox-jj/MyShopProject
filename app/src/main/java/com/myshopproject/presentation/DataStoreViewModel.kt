package com.myshopproject.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.preferences.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val pref: MyPreferences
) : ViewModel() {

    val getRefreshToken = pref.getRefreshToken()
    val getAccessToken = pref.getAccessToken()
    val getUserId = pref.getUserId()
    val getEmailUser = pref.getEmailUser()
    val getGenderUser = pref.getGenderUser()
    val getNameUser = pref.getNameUser()
    val getPhoneNumber = pref.getPhoneNumber()
    val getImageUser = pref.getImageUser()
    val getLanguage = pref.getLanguage()
    val getAuthRefresh = pref.getAuthRefresh()

    fun saveRefreshToken(refreshToken: String) {
        viewModelScope.launch {
            pref.saveRefreshToken(refreshToken)
        }
    }

    fun saveAccessToken(accessToken: String) {
        viewModelScope.launch {
            pref.saveAccessToken(accessToken)
        }
    }

    fun saveUserId(id: Int) {
        viewModelScope.launch {
            pref.saveUserId(id)
        }
    }

    fun saveEmailUser(email: String) {
        viewModelScope.launch {
            pref.saveEmailUser(email)
        }
    }

    fun saveGenderUser(gender: Int) {
        viewModelScope.launch {
            pref.saveGenderUser(gender)
        }
    }

    fun saveNameUser(name: String) {
        viewModelScope.launch {
            pref.saveNameUser(name)
        }
    }

    fun savePhoneNumber(phone: String) {
        viewModelScope.launch {
            pref.savePhoneNumber(phone)
        }
    }

    fun saveImageUser(image: String) {
        viewModelScope.launch {
            pref.saveImageUser(image)
        }
    }

    fun saveLanguage(language: Int) {
        viewModelScope.launch {
            pref.saveLanguage(language)
        }
    }

    fun saveAuthRefresh(userId: Int, accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            pref.saveAuthRefresh(userId, accessToken, refreshToken)
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            pref.clearSession()
        }
    }

}