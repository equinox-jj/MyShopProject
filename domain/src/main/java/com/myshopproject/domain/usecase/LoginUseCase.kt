package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<LoginResult>> {
        return authRepository.loginAccount(email = email, password = password)
    }
}