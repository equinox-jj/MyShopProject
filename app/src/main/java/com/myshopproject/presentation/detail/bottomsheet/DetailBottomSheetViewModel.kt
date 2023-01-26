package com.myshopproject.presentation.detail.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailBottomSheetViewModel : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private var initPrice: Int? = 0

    init {
        _quantity.value = 1
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