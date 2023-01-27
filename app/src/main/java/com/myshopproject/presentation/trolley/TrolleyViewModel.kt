package com.myshopproject.presentation.trolley

import androidx.lifecycle.ViewModel
import com.myshopproject.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrolleyViewModel @Inject constructor(
    private val localUseCase: LocalUseCase
) : ViewModel() {

}