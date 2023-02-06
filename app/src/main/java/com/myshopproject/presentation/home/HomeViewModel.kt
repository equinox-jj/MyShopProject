package com.myshopproject.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.myshopproject.domain.usecase.RemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private var searchJob: Job? = null

    private val currentQuery = MutableStateFlow("")

    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                currentQuery.value = query
            } else {
                delay(2000)
                currentQuery.value = query
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productList = currentQuery.flatMapLatest { query ->
        remoteUseCase.getListProductPaging(query).cachedIn(viewModelScope)
    }
}