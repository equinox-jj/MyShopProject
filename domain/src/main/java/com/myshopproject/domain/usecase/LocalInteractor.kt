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
    override suspend fun updateCartData(q: Int?, totalPriceCart: Int?, id: Int?) = productRepository.updateCartData(q, totalPriceCart, id)

    override suspend fun updateCartIsCheckedAll(isChecked: Boolean) = productRepository.updateCartIsCheckedAll(isChecked)

    override suspend fun updateCartIsCheckedById(isChecked: Boolean, id: Int?) = productRepository.updateCartIsCheckedById(isChecked, id)

    override fun getAllCarts(): Flow<List<CartEntity>> = productRepository.getAllCarts()
    override fun getCartIsChecked(): Flow<List<CartEntity>> = productRepository.getCartIsChecked()

    override fun getCartById(id: Int): Flow<List<CartEntity>> = productRepository.getCartById(id)
}