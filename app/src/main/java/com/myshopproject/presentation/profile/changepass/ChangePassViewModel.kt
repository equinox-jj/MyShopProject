package com.myshopproject.presentation.profile.changepass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePassViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<SuccessResponseStatus>>()
    val state: LiveData<Resource<SuccessResponseStatus>> = _state

    fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            remoteUseCase.changePassword(
                id = id,
                password = password,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            ).collect { response ->
                _state.value = response
            }
        }
    }

}