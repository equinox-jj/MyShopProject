package com.myshopproject.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<LoginResult>>()
    val state: LiveData<Resource<LoginResult>> = _state

    fun loginAccount(email: String, password: String, firebaseToken: String) {
        remoteUseCase.loginAccount(email, password, firebaseToken).onEach { response ->
            when(response) {
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

}