package com.myshopproject.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.entities.DetailProductResponse
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
) : ViewModel() {

    private val _detailState = MutableLiveData<Resource<DetailProductResponse>>()
    val detailState: LiveData<Resource<DetailProductResponse>> = _detailState

    private val _favState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val favState: LiveData<Resource<SuccessResponseStatus>> = _favState

    private val _unFavState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val unFavState: LiveData<Resource<SuccessResponseStatus>> = _unFavState

    private val _otherProductState = MutableLiveData<Resource<DataProductResponse>>()
    val otherProductState: LiveData<Resource<DataProductResponse>> = _otherProductState

    private val _historyProductState = MutableLiveData<Resource<DataProductResponse>>()
    val historyProductState: LiveData<Resource<DataProductResponse>> = _historyProductState

    fun onRefresh(productId: Int, userId: Int) {
        getProductDetail(productId, userId)
    }

    fun getProductDetail(productId: Int, userId: Int) {
        viewModelScope.launch {
            remoteUseCase.getProductDetail(productId, userId).collect { response ->
                _detailState.value = response
            }
        }
    }

    fun getProductOther(userId: Int) {
        viewModelScope.launch {
            remoteUseCase.getProductOther(userId).collect { response ->
                _otherProductState.value = response
            }
        }
    }

    fun getProductHistory(userId: Int) {
        viewModelScope.launch {
            remoteUseCase.getProductHistory(userId).collect { response ->
                _historyProductState.value = response
            }
        }
    }

    fun addProductFavorite(productId: Int, userId: Int) {
        viewModelScope.launch {
            remoteUseCase.addProductFavorite(productId, userId).collect { response ->
                _favState.value = response
            }
        }
    }

    fun removeProductFavorite(productId: Int, userId: Int) {
        viewModelScope.launch {
            remoteUseCase.removeProductFavorite(productId, userId).collect { response ->
                _unFavState.value = response
            }
        }
    }
}