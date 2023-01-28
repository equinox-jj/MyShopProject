package com.myshopproject.presentation.buysuccess

import androidx.lifecycle.ViewModel
import com.myshopproject.domain.usecase.RemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuySuccessViewModel @Inject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {


}