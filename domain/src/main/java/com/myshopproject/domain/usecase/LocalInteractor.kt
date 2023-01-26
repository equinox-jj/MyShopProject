package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalInteractor @Inject constructor(
    private val productRepository: ProductRepository
): LocalUseCase {
    override suspend fun insertCart(cartEntity: CartEntity) = productRepository.insertCart(cartEntity)
    override suspend fun deleteCart(id: Int) = productRepository.deleteCart(id)
    override fun getAllCarts(): Flow<List<CartEntity>> = productRepository.getAllCarts()
}