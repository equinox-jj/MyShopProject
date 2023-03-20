package com.myshopproject.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.PaymentTypeResponse
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            remoteUseCase.getPaymentMethod().collect { response ->
                _state.value = response
            }
        }
    }
}