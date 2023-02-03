package com.myshopproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(private val localUseCase: LocalUseCase) : ViewModel() {

    fun getAllProduct(): Flow<List<CartEntity>> {
        return localUseCase.getAllProduct()
    }

    fun getAllCheckedProduct(): Flow<List<CartEntity>> {
        return localUseCase.getAllCheckedProduct()
    }

    fun insertCart(cartEntity: CartEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            localUseCase.addProductToTrolley(cartEntity)
        }
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

}