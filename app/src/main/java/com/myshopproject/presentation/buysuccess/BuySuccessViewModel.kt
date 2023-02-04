package com.myshopproject.presentation.buysuccess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.entities.UpdateRate
import com.myshopproject.domain.usecase.LocalUseCase
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuySuccessViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<SuccessResponseStatus>>()
    val state: LiveData<Resource<SuccessResponseStatus>> = _state

    fun updateRate(id: Int, updateRate: UpdateRate) {
        remoteUseCase.updateRate(id, updateRate).onEach { response ->
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

    fun deleteProductByIdFromTrolley(id: Int?) {
        viewModelScope.launch {
            localUseCase.deleteProductByIdFromTrolley(id)
        }
    }
}