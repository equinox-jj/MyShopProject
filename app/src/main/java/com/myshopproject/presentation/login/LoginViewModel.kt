package com.myshopproject.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    private val pref: MyPreferences
) : ViewModel() {

    private val _state = MutableLiveData<Resource<LoginResult>>()
    val state: LiveData<Resource<LoginResult>> = _state

    val getAccessToken = pref.getAccessToken()

    fun loginAccount(email: String, password: String) {
        remoteUseCase.loginAccount(email, password).onEach { response ->
            when(response) {
                is Resource.Loading -> {
                    _state.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _state.value = Resource.Success(it)
                        pref.saveAuthRefresh(
                            it.dataUser.id,
                            it.accessToken,
                            it.refreshToken
                        )
                        pref.saveEmailUser(it.dataUser.email)
                        pref.saveGenderUser(it.dataUser.gender)
                        pref.saveNameUser(it.dataUser.name)
                        pref.savePhoneNumber(it.dataUser.phone)
                        pref.saveImageUser(it.dataUser.path)
                    }
                }
                is Resource.Error -> {
                    _state.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

}