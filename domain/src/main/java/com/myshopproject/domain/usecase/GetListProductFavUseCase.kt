package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListProductFavUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(query: String, userId: Int): Flow<Resource<DataProductResponse>> = authRepository.getListProductFavorite(query, userId)
}