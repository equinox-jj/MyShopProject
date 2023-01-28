package com.myshopproject.presentation.trolley

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.myshopproject.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrolleyViewModel @Inject constructor(
    localUseCase: LocalUseCase
) : ViewModel() {

    val getAllCarts = localUseCase.getAllCarts().asLiveData()
//    val getAllCartss = localUseCase.getAllCarts().asLiveData()

}