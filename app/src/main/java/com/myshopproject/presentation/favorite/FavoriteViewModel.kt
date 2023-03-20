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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase,
    private val pref: MyPreferences
) : ViewModel() {

    private val _state = MutableLiveData<Resource<DataProductResponse>>()
    val state: LiveData<Resource<DataProductResponse>> = _state

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            getProductListFav("", pref.getUserId().first())
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            getProductListFav("", pref.getUserId().first())
        }
    }
    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            if (query.isEmpty()) {
                getProductListFav("", pref.getUserId().first())
            } else {
                delay(2000)
                getProductListFav(query, pref.getUserId().first())
            }
        }
    }

    private fun getProductListFav(query: String?, userId: Int) {
        viewModelScope.launch {
            remoteUseCase.getListProductFavorite(query, userId).collect { response ->
                _state.value = response
            }
        }
    }

}