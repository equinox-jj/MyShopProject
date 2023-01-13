package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.repository.CreateAccountRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val createAccountRepository: CreateAccountRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<LoginResult>> {
        return createAccountRepository.loginAccount(email, password)
    }
}