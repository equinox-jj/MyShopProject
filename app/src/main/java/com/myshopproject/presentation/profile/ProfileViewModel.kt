package com.myshopproject.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.ChangeImageResponse
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    private val pref: MyPreferences
) : ViewModel() {

    private val _state = MutableLiveData<Resource<ChangeImageResponse>>()
    val state: LiveData<Resource<ChangeImageResponse>> = _state

    val getNameUser = pref.getNameUser()
    val getEmailUser = pref.getEmailUser()
    val getImageUser = pref.getImageUser()
    val getUserId = pref.getUserId()
    val getLanguage = pref.getLanguage()

    fun saveLanguagePref(language: Int) {
        viewModelScope.launch {
            pref.saveLanguage(language)
        }
    }

    fun saveImageUser(image: String) {
        viewModelScope.launch {
            pref.saveImageUser(image)
        }
    }

    fun changeImage(id: Int, image: MultipartBody.Part) {
        remoteUseCase.changeImage(id = id, image = image).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _state.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _state.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _state.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun removeSession() {
        viewModelScope.launch {
            pref.clearSession()
        }
    }

}