package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        image: MultipartBody.Part,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int
    ): Flow<Resource<SuccessResponseStatus>> {
        return authRepository.registerAccount(
            image = image,
            email = email,
            password = password,
            name = name,
            phone = phone,
            gender = gender
        )
    }
}