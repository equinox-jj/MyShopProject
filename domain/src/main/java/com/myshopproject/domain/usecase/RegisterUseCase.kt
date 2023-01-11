package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.DataRegister
import com.myshopproject.domain.entities.DataUserResponse
import com.myshopproject.domain.entities.RegisterResponse
import com.myshopproject.domain.repository.CreateAccountRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val createAccountRepository: CreateAccountRepository
) {
    operator fun invoke(dataUser: DataRegister): Flow<Resource<RegisterResponse>> {
        return createAccountRepository.registerAccount(dataUser)
    }
}