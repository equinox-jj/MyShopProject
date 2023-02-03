package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartEntity
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {
    fun getAllProduct(): Flow<List<CartEntity>>
    fun getAllCheckedProduct(): Flow<List<CartEntity>>
    suspend fun addProductToTrolley(trolley: CartEntity)
    suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?)
    suspend fun updateProductIsCheckedAll(isChecked: Boolean)
    suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?)
    suspend fun deleteProductByIdFromTrolley(id: Int?)
}