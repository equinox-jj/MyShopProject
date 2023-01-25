package com.myshopproject.presentation.detail.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailBottomSheetViewModel : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    fun increaseQuantity() {

    }

    fun decreaseQuantity() {

    }

}