package com.myshopproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.domain.entities.FcmDataDomain
import com.myshopproject.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(private val localUseCase: LocalUseCase) : ViewModel() {

    fun getAllProduct(): Flow<List<CartDataDomain>> {
        return localUseCase.getAllProduct()
    }

    fun getAllCheckedProduct(): Flow<List<CartDataDomain>> {
        return localUseCase.getAllCheckedProduct()
    }

    fun insertCart(cartDataDomain: CartDataDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            localUseCase.addProductToTrolley(cartDataDomain)
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

    fun getAllNotification(): Flow<List<FcmDataDomain>> {
        return localUseCase.getAllNotification()
    }

    fun updateReadNotification(isRead: Boolean, id: Int?) {
        viewModelScope.launch {
            localUseCase.updateReadNotification(isRead, id)
        }
    }

    fun setAllReadNotification(isRead: Boolean) {
        viewModelScope.launch {
            localUseCase.setAllReadNotification(isRead)
        }
    }

    fun updateCheckedNotification(isChecked: Boolean, id: Int?) {
        viewModelScope.launch {
            localUseCase.updateCheckedNotification(isChecked, id)
        }
    }

    fun setAllUncheckedNotification(isChecked: Boolean = false) {
        viewModelScope.launch {
            localUseCase.setAllUncheckedNotification(isChecked)
        }
    }

    fun deleteNotification(isChecked: Boolean) {
        viewModelScope.launch {
            localUseCase.deleteNotification(isChecked)
        }
    }
}