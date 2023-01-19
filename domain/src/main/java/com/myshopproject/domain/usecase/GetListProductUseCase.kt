package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListProductUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Resource<DataProductResponse>> = authRepository.getListProduct()
}