package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePassUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<SuccessResponseStatus>> {
        return authRepository.changePassword(
            id = id,
            password = password,
            newPassword = newPassword,
            confirmPassword = confirmPassword
        )
    }
}