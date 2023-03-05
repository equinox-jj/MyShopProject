package com.myshopproject.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.PaymentTypeResponse
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<List<PaymentTypeResponse>>>()
    val state: LiveData<Resource<List<PaymentTypeResponse>>> = _state

    init {
        getPaymentMethod()
    }

    private fun getPaymentMethod() {
        remoteUseCase.getPaymentMethod().onEach { response ->
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
}