package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.ChangeImageResponse
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ChangeImageUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(id: Int, image: MultipartBody.Part): Flow<Resource<ChangeImageResponse>> {
        return authRepository.changeImage(id = id, image = image)
    }
}