package com.myshopproject.presentation.trolley

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.entities.UpdateStockItem
import com.myshopproject.domain.entities.UpdateStockProduct
import com.myshopproject.domain.usecase.LocalUseCase
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrolleyViewModel @Inject constructor(
    private val localUseCase: LocalUseCase,
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _updateStockState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val updateStockState: LiveData<Resource<SuccessResponseStatus>> = _updateStockState

    fun updateStock(data: List<UpdateStockItem>) {
        remoteUseCase.updateStock(UpdateStockProduct(data)).onEach { response ->
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

    fun getAllProduct(): Flow<List<CartEntity>> {
        return localUseCase.getAllProduct()
    }

    fun getAllCheckedProduct(): Flow<List<CartEntity>> {
        return localUseCase.getAllCheckedProduct()
    }

    fun getProductById(id: Int?): Flow<List<CartEntity>> {
        return localUseCase.getProductById(id)
    }

    fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?) {
        viewModelScope.launch {
            localUseCase.updateProductData(quantity, itemTotalPrice, id)
        }
    }

    fun updateProductIsCheckedAll(isChecked: Boolean) {
        viewModelScope.launch {
            localUseCase.updateProductIsCheckedAll(isChecked)
        }
    }

    fun updateProductIsCheckedById(isChecked: Boolean, id: Int?) {
        viewModelScope.launch {
            localUseCase.updateProductIsCheckedById(isChecked, id)
        }
    }

    fun deleteProductByIdFromTrolley(id: Int?) {
        viewModelScope.launch {
            localUseCase.deleteProductByIdFromTrolley(id)
        }
    }

    fun deleteAllProductFromTrolley(cartEntity: CartEntity) {
        viewModelScope.launch {
            localUseCase.deleteAllProductFromTrolley(cartEntity)
        }
    }
}