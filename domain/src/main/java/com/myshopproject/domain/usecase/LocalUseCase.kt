package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartEntity
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {
    suspend fun insertCart(cartEntity: CartEntity)
    suspend fun deleteCart(id: Int)
    fun getAllCarts(): Flow<List<CartEntity>>
}