package com.myshopproject.presentation.auth.login

import com.myshopproject.domain.entities.LoginResult

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: LoginResult? = null,
    val isError: String? = null,
)
