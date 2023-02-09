package com.myshopproject.presentation.trolley

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.entities.UpdateStockItem
import com.myshopproject.domain.entities.UpdateStockProduct
import com.myshopproject.domain.usecase.LocalUseCase
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrolleyViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _updateStockState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val updateStockState: LiveData<Resource<SuccessResponseStatus>> = _updateStockState

    fun updateStock(userId: String, data: List<UpdateStockItem>) {
        remoteUseCase.updateStock(UpdateStockProduct(userId, data)).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _updateStockState.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _updateStockState.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _updateStockState.value = Resource.Error(response.message, response.errorCode, response.errorBody)
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