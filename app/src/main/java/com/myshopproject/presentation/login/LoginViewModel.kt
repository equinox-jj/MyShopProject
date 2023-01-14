package com.myshopproject.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.usecase.LoginUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val pref: MyPreferences
) : ViewModel() {

    private val _state = MutableLiveData<Resource<LoginResult>>()
    val state: LiveData<Resource<LoginResult>> = _state

    val getUserSession = pref.getSession()

    fun loginAccount(email: String, password: String) {
        loginUseCase.invoke(email, password).onEach { response ->
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
                    _state.value = Resource.Error(true, response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

//    fun loginAccount(email: String, password: String) {
//        viewModelScope.launch {
//            loginUseCase.invoke(email, password)
//                .onStart {
//                    _state.value = Resource.Loading
//                }
//                .collect { response ->
//                    response.data?.let { result ->
//                        _state.value = Resource.Success(result)
//                        pref.saveSession(
//                            result.refreshToken,
//                            result.accessToken,
//                            result.dataUser.id,
//                            result.dataUser.email,
//                            result.dataUser.gender,
//                            result.dataUser.name,
//                            result.dataUser.phone
//                        )
//                    }
//                }
//        }
//    }

}