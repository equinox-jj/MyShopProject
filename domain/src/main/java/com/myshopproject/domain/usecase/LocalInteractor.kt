package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.domain.entities.FcmDataDomain
import com.myshopproject.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalInteractor @Inject constructor(
    private val productRepository: ProductRepository
) : LocalUseCase {
    override fun getAllProduct(): Flow<List<CartDataDomain>> {
        return productRepository.getAllProduct()
    }

    override fun getAllCheckedProduct(): Flow<List<CartDataDomain>> {
        return productRepository.getAllCheckedProduct()
    }

    override suspend fun addProductToTrolley(trolley: CartDataDomain) {
        productRepository.addProductToTrolley(trolley)
    }

    override suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?) {
        productRepository.updateProductData(quantity, itemTotalPrice, id)
    }

    override suspend fun updateProductIsCheckedAll(isChecked: Boolean) {
        productRepository.updateProductIsCheckedAll(isChecked)
    }

    override suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?) {
        productRepository.updateProductIsCheckedById(isChecked, id)
    }

    override suspend fun deleteProductByIdFromTrolley(id: Int?) {
        productRepository.deleteProductByIdFromTrolley(id)
    }

    override suspend fun insertNotification(fcmDataDomain: FcmDataDomain) {
        productRepository.insertNotification(fcmDataDomain)
    }

    override fun getAllNotification(): Flow<List<FcmDataDomain>> {
        return productRepository.getAllNotification()
    }

    override suspend fun updateReadNotification(isRead: Boolean, id: Int?) {
        productRepository.updateReadNotification(isRead, id)
    }

    override suspend fun setAllReadNotification(isRead: Boolean) {
        productRepository.setAllReadNotification(isRead)
    }

    override suspend fun updateCheckedNotification(isChecked: Boolean, id: Int?) {
        productRepository.updateCheckedNotification(isChecked, id)
    }

    override suspend fun setAllUncheckedNotification(isChecked: Boolean) {
        productRepository.setAllUncheckedNotification(isChecked)
    }

    override suspend fun deleteNotification(isChecked: Boolean) {
        productRepository.deleteNotification(isChecked)
    }
}