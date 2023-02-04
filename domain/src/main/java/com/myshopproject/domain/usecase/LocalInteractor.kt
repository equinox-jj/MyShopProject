package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalInteractor @Inject constructor(
    private val productRepository: ProductRepository
) : LocalUseCase {
    override fun getAllProduct(): Flow<List<CartEntity>> {
        return productRepository.getAllProduct()
    }

    override fun getAllCheckedProduct(): Flow<List<CartEntity>> {
        return productRepository.getAllCheckedProduct()
    }

    override suspend fun addProductToTrolley(trolley: CartEntity) {
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
}