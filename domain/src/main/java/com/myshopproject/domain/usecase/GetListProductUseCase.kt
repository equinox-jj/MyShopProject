package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    operator fun invoke(query: String?): Flow<Resource<DataProductResponse>> = productRepository.getListProduct(query)
}