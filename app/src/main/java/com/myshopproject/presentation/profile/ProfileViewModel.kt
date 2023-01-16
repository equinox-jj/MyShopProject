package com.myshopproject.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.preferences.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val pref: MyPreferences
) : ViewModel() {

    val getNameUser = pref.getNameUser()
    val getEmailUser = pref.getEmailUser()

    fun removeSession() {
        viewModelScope.launch {
            pref.removeSession()
        }
    }

}