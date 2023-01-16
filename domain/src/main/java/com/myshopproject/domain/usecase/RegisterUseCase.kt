package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.RegisterResponse
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
//        image: MultipartBody.Part,
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: Int
    ): Flow<Resource<RegisterResponse>> {
        return authRepository.registerAccount(
//            image = image,
            email = email,
            password = password,
            name = name,
            phone = phone,
            gender = gender
        )
    }
}