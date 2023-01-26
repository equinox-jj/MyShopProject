package com.myshopproject.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.preferences.MyPreferences
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    pref: MyPreferences
) : ViewModel() {

    private val _state = MutableLiveData<Resource<DataProductResponse>>()
    val state: LiveData<Resource<DataProductResponse>> = _state

    val getUserId = pref.getUserId()

    init {
        viewModelScope.launch {
            getProductListFav("", getUserId.first())
        }
    }

    fun getProductListFav(query: String?, userId: Int) {
        remoteUseCase.getListProductFavorite(query, userId).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _state.value = Resource.Loading
                }
                is Resource.Success -> {
                    response.data?.let { result ->
                        _state.value = Resource.Success(result)
                    }
                }
                is Resource.Error -> {
                    _state.value = Resource.Error(response.message, response.errorCode, response.errorBody)
                }
            }
        }.launchIn(viewModelScope)
    }

}