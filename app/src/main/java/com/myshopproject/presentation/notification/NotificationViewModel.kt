package com.myshopproject.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopproject.domain.entities.FcmDataDomain
import com.myshopproject.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val localUseCase: LocalUseCase
): ViewModel() {

    fun getAllNotification(): Flow<List<FcmDataDomain>> {
        return localUseCase.getAllNotification()
    }

     fun insertNotification(fcmDataDomain: FcmDataDomain) {
        viewModelScope.launch {
            localUseCase.insertNotification(fcmDataDomain)
        }
    }
    
     fun updateReadNotification(isRead: Boolean, id: Int?) {
         viewModelScope.launch {
             localUseCase.updateReadNotification(isRead, id)
         }
    }
    
     fun setAllReadNotification(isRead: Boolean) {
         viewModelScope.launch {
             localUseCase.setAllReadNotification(isRead)
         }
    }
    
     fun updateCheckedNotification(isChecked: Boolean, id: Int?) {
         viewModelScope.launch {
             localUseCase.updateCheckedNotification(isChecked, id)
         }
    }
    
     fun setAllUncheckedNotification(isChecked: Boolean = false) {
         viewModelScope.launch {
             localUseCase.setAllUncheckedNotification(isChecked)
         }
    }
    
     fun deleteNotification(isChecked: Boolean) {
         viewModelScope.launch {
             localUseCase.deleteNotification(isChecked)
         }
    }
}