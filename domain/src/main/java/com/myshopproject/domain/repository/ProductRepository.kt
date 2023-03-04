package com.myshopproject.domain.repository

import androidx.paging.PagingData
import com.myshopproject.domain.entities.*
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    /** API SERVICE */
    fun getListProductPaging(query: String?) : Flow<PagingData<DataProduct>>
    fun getListProductFavorite(query: String?, userId: Int): Flow<Resource<DataProductResponse>>
    fun getProductDetail(productId: Int, userId: Int): Flow<Resource<DetailProductResponse>>
    fun addProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>>
    fun removeProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>>
    fun updateStock(updateStock: UpdateStockProduct): Flow<Resource<SuccessResponseStatus>>
    fun updateRate(id: Int, updateRate: String): Flow<Resource<SuccessResponseStatus>>
    fun getProductOther(userId: Int): Flow<Resource<DataProductResponse>>
    fun getProductHistory(userId: Int): Flow<Resource<DataProductResponse>>

    /** ROOM DB */
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