package com.myshopproject.presentation.detail.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.entities.UpdateStockItem
import com.myshopproject.domain.entities.UpdateStockProduct
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailBottomSheetViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val _updateStockState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val updateStockState: LiveData<Resource<SuccessResponseStatus>> = _updateStockState

    private var initPrice: Int? = 0

    init {
        _quantity.value = 1
    }

    fun updateStock(idProduct: String, stock: Int) {
        remoteUseCase.updateStock(UpdateStockProduct(listOf(UpdateStockItem(idProduct, stock)))).onEach { response ->
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

    fun increaseQuantity(stock: Int?) {
        if (_quantity.value!! < stock!!) {
            _quantity.value = _quantity.value?.plus(1)
            _price.value = initPrice?.times(_quantity.value!!.toInt())
        }
    }

    fun decreaseQuantity() {
        if (quantity.value == 1) {
            _quantity.value = 1
            _price.value = initPrice!!.toInt()
        } else {
            _quantity.value = _quantity.value?.minus(1)
            _price.value = initPrice?.times(_quantity.value!!.toInt())
        }
    }

    fun setPrice(productPrice: Int) {
        initPrice = productPrice
        _price.value = productPrice
    }

}