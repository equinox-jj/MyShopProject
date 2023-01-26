package com.myshopproject.domain.repository

import com.myshopproject.domain.entities.*
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getListProduct(query: String?): Flow<Resource<DataProductResponse>>
    fun getListProductFavorite(query: String?, userId: Int): Flow<Resource<DataProductResponse>>
    fun getProductDetail(productId: Int, userId: Int): Flow<Resource<DetailProductResponse>>
    fun addProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>>
    fun removeProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>>
    fun updateStock(updateStock: UpdateStockProduct): Flow<Resource<SuccessResponseStatus>>
    fun updateRate(id: Int, updateRate: UpdateRate): Flow<Resource<SuccessResponseStatus>>

    suspend fun insertCart(cartEntity: CartEntity)
    suspend fun deleteCart(id: Int)
    fun getAllCarts(): Flow<List<CartEntity>>
}