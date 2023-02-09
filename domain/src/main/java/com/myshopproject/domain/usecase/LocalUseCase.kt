package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartDataDomain
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {
    fun getAllProduct(): Flow<List<CartDataDomain>>
    fun getAllCheckedProduct(): Flow<List<CartDataDomain>>
    suspend fun addProductToTrolley(trolley: CartDataDomain)
    suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?)
    suspend fun updateProductIsCheckedAll(isChecked: Boolean)
    suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?)
    suspend fun deleteProductByIdFromTrolley(id: Int?)
}