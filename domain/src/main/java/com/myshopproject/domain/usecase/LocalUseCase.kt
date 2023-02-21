package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.domain.entities.FcmDataDomain
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {
    fun getAllProduct(): Flow<List<CartDataDomain>>
    fun getAllCheckedProduct(): Flow<List<CartDataDomain>>
    fun checkProductDataCart(id: Int?, name: String?): Int
    suspend fun addProductToTrolley(trolley: CartDataDomain)
    suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?)
    suspend fun updateProductIsCheckedAll(isChecked: Boolean)
    suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?)
    suspend fun deleteProductByIdFromTrolley(id: Int?)

    suspend fun insertNotification(fcmDataDomain: FcmDataDomain)
    fun getAllNotification(): Flow<List<FcmDataDomain>>
    suspend fun updateReadNotification(isRead: Boolean, id: Int?)
    suspend fun setAllReadNotification(isRead: Boolean)
    suspend fun updateCheckedNotification(isChecked: Boolean, id: Int?)
    suspend fun setAllUncheckedNotification(isChecked: Boolean)
    suspend fun deleteNotification(isChecked: Boolean)
}