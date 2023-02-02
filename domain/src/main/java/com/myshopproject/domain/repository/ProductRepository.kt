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

    fun getAllProduct(): Flow<List<CartEntity>>
    fun getAllCheckedProduct(): Flow<List<CartEntity>>
    fun getProductById(id: Int?): Flow<List<CartEntity>>
    suspend fun addProductToTrolley(trolley: CartEntity)
    suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?)
    suspend fun updateProductIsCheckedAll(isChecked: Boolean)
    suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?)
    suspend fun deleteProductByIdFromTrolley(id: Int?)
    suspend fun deleteAllProductFromTrolley(trolley: CartEntity)
}