package com.myshopproject.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.entities.DetailProductResponse
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.usecase.LocalUseCase
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _detailState = MutableLiveData<Resource<DetailProductResponse>>()
    val detailState: LiveData<Resource<DetailProductResponse>> = _detailState

    private val _favState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val favState: LiveData<Resource<SuccessResponseStatus>> = _favState

    private val _unFavState = MutableLiveData<Resource<SuccessResponseStatus>>()
    val unFavState: LiveData<Resource<SuccessResponseStatus>> = _unFavState

    fun onRefresh(productId: Int, userId: Int) {
        getProductDetail(productId, userId)
    }

    fun getProductDetail(productId: Int, userId: Int) {
        remoteUseCase.getProductDetail(productId, userId).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _detailState.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _detailState.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _detailState.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addProductFavorite(productId: Int, userId: Int) {
        remoteUseCase.addProductFavorite(productId, userId).onEach { response ->
            when(response) {
                is Resource.Loading -> {
                    _favState.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _favState.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _favState.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun removeProductFavorite(productId: Int, userId: Int) {
        remoteUseCase.removeProductFavorite(productId, userId).onEach { response ->
            when(response) {
                is Resource.Loading -> {
                    _unFavState.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _unFavState.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _unFavState.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun insertCart(cartEntity: CartEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            localUseCase.addProductToTrolley(cartEntity)
        }
    }
}