package com.myshopproject.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.ChangeImageResponse
import com.myshopproject.domain.usecase.RemoteUseCase
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private val _state = MutableLiveData<Resource<ChangeImageResponse>>()
    val state: LiveData<Resource<ChangeImageResponse>> = _state

    fun changeImage(id: Int, image: MultipartBody.Part) {
        viewModelScope.launch {
            remoteUseCase.changeImage(id = id, image = image).collect { response ->
                _state.value = response
            }
        }
    }

}