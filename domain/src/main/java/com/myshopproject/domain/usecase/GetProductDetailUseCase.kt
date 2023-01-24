package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.DetailProductResponse
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(productId: Int): Flow<Resource<DetailProductResponse>> {
        return productRepository.getProductDetail(productId)
    }
}