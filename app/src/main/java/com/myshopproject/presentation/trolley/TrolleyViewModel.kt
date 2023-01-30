package com.myshopproject.presentation.trolley

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrolleyViewModel @Inject constructor(
    private val localUseCase: LocalUseCase
) : ViewModel() {

    val getAllCarts = localUseCase.getAllCarts().asLiveData()
//    val getAllCartss = localUseCase.getAllCarts().asLiveData()
    fun deleteSingleProduct(id: Int) {
        viewModelScope.launch {
            localUseCase.deleteCart(id)
        }
    }
}