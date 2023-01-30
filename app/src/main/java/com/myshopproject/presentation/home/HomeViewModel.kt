package com.myshopproject.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<DataProductResponse>>()
    val state: LiveData<Resource<DataProductResponse>> = _state

    private var searchJob: Job? = null

    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            if (query.isEmpty()) {
                getProductList("")
            } else {
                getProductList(query)
            }
        }
    }
    private fun getProductList(query: String?) {
        remoteUseCase.getListProduct(query).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _state.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let {
                        _state.value = Resource.Success(it)
                    }
                }
                is Resource.Error -> {
                    _state.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }
}