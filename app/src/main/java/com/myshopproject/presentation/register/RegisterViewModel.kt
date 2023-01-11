package com.myshopproject.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.DataRegister
import com.myshopproject.domain.entities.DataUserResponse
import com.myshopproject.domain.entities.RegisterResponse
import com.myshopproject.domain.usecase.RegisterUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<RegisterResponse>>()
    val state: LiveData<Resource<RegisterResponse>> = _state

    fun registerAccount(dataUser: DataRegister) {
        viewModelScope.launch {
            registerUseCase.invoke(dataUser)
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
                    }
                }
        }
    }
}