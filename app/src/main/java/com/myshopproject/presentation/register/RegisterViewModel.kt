package com.myshopproject.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<SuccessResponseStatus>>()
    val state: LiveData<Resource<SuccessResponseStatus>> = _state

    fun registerAccount(
        image: MultipartBody.Part,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int
    ) {
        viewModelScope.launch {
            remoteUseCase.registerAccount(
                image = image,
                email = email,
                password = password,
                name = name,
                phone = phone,
                gender = gender
            ).collect { response ->
                _state.value = response
            }
        }
    }

}