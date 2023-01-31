package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartEntity
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {
    suspend fun insertCart(cartEntity: CartEntity)
    suspend fun deleteCart(id: Int)
    suspend fun updateCartData(q: Int?, totalPriceCart: Int?, id: Int?)
    suspend fun updateCartIsCheckedAll(isChecked: Boolean)
    suspend fun updateCartIsCheckedById(isChecked: Boolean, id: Int?)
    fun getAllCarts(): Flow<List<CartEntity>>
    fun getCartIsChecked(): Flow<List<CartEntity>>
    fun getCartById(id: Int): Flow<List<CartEntity>>
}