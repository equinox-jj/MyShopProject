package com.myshopproject.presentation.buysuccess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuySuccessViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<SuccessResponseStatus>>()
    val state: LiveData<Resource<SuccessResponseStatus>> = _state

    fun updateRate(id: Int, updateRate: String) {
        viewModelScope.launch {
            remoteUseCase.updateRate(id, updateRate).collect { response ->
                _state.value = response
            }
        }
    }
}