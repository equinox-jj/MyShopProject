package com.myshopproject.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.myshopproject.domain.usecase.RemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private var searchJob: Job? = null

    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                getListProductPaging("")
            } else {
                delay(2000)
                getListProductPaging(query)
            }
        }
    }

    fun getListProductPaging(query: String?) = remoteUseCase.getListProductPaging(query).cachedIn(viewModelScope)
}