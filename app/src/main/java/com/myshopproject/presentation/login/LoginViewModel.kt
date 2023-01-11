package com.myshopproject.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.usecase.LoginUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            loginUseCase.invoke(email, password)
                .onStart {
                    _state.value = Resource.Loading()
                }
                .catch { error ->
                    error.message?.let { message ->
                        _state.value = Resource.Error(message)
                    }
                }
                .collect { response ->
                    response.data?.let { result ->
                        _state.value = Resource.Success(result)
                        pref.saveSession(result.accessToken)
                        Log.d("UserToken", "loginAccount: ${result.accessToken}")
                    }
                }
        }
    }

}