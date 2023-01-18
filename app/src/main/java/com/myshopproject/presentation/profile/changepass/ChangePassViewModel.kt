package com.myshopproject.presentation.profile.changepass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.usecase.ChangePassUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChangePassViewModel @Inject constructor(
    private val changePassUseCase: ChangePassUseCase,
    pref: MyPreferences
) : ViewModel() {

    private val _state = MutableLiveData<Resource<SuccessResponseStatus>>()
    val state: LiveData<Resource<SuccessResponseStatus>> = _state

    val getAuthorization = pref.getRefreshToken()
    val getUserId = pref.getUserId()

    fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ) {
        changePassUseCase.invoke(
            id = id,
            password = password,
            newPassword = newPassword,
            confirmPassword = confirmPassword
        ).onEach { response ->
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