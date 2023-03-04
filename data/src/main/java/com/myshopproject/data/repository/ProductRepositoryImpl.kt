package com.myshopproject.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.mapper.toEntity
import com.myshopproject.data.source.local.dao.FirebaseMessageDao
import com.myshopproject.data.source.local.dao.ProductCartDao
import com.myshopproject.data.source.remote.RemotePagingSource
import com.myshopproject.data.source.remote.RemotePagingSource.Companion.TOTAL_ITEM
import com.myshopproject.data.source.remote.network.ApiProduct
import com.myshopproject.domain.entities.*
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiProduct: ApiProduct,
    private val cartDao: ProductCartDao,
    private val fcmDao: FirebaseMessageDao
) : ProductRepository {

    /** API SERVICE */
    override fun getListProductPaging(query: String?): Flow<PagingData<DataProduct>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = TOTAL_ITEM,
                initialLoadSize = TOTAL_ITEM,
//                prefetchDistance = 1
            ),
            pagingSourceFactory = { RemotePagingSource(query = query, apiProduct = apiProduct) }
        ).flow
    }

    override fun getListProductFavorite(
        query: String?,
        userId: Int
    ): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getListProductFavorite(query = query, userId = userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun getProductDetail(productId: Int, userId: Int): Flow<Resource<DetailProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getProductDetail(productId = productId, userId = userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun addProductFavorite(
        productId: Int,
        userId: Int
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.addProductFavorite(productId = productId, userId = userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun removeProductFavorite(
        productId: Int,
        userId: Int
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.removeProductFavorite(productId = productId, userId = userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun updateStock(updateStock: UpdateStockProduct): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.updateStock(updateStock = updateStock).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun updateRate(
        id: Int,
        updateRate: String
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.updateRate(id = id, rate = updateRate).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun getProductOther(userId: Int): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getProductOther(userId = userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun getProductHistory(userId: Int): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getProductHistory(userId = userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    /** ROOM DB */
    override fun getAllProduct(): Flow<List<CartDataDomain>> {
        return cartDao.getAllProduct().map { it.toDomain() }
    }

    override fun getAllCheckedProduct(): Flow<List<CartDataDomain>> {
        return cartDao.getAllCheckedProduct().map { it.toDomain() }
    }

    override fun checkProductDataCart(id: Int?, name: String?): Int {
        return cartDao.checkProductDataCart(id, name)
    }

    override suspend fun addProductToTrolley(trolley: CartDataDomain) {
        cartDao.addProductToTrolley(trolley = trolley.toEntity())
    }

    override suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?) {
        cartDao.updateProductData(quantity = quantity, itemTotalPrice = itemTotalPrice, id = id)
    }

    override suspend fun updateProductIsCheckedAll(isChecked: Boolean) {
        cartDao.updateProductIsCheckedAll(isChecked = isChecked)
    }

    override suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?) {
        cartDao.updateProductIsCheckedById(isChecked = isChecked, id = id)
    }

    override suspend fun deleteProductByIdFromTrolley(id: Int?) {
        cartDao.deleteProductByIdFromTrolley(id = id)
    }

    override suspend fun insertNotification(fcmDataDomain: FcmDataDomain) {
        fcmDao.insertNotification(fcmEntity = fcmDataDomain.toEntity())
    }

    override fun getAllNotification(): Flow<List<FcmDataDomain>> {
        return fcmDao.getAllNotification().map { it.toDomain()}
    }

    override suspend fun updateReadNotification(isRead: Boolean, id: Int?) {
        fcmDao.updateReadNotification(isRead = isRead, id = id)
    }

    override suspend fun setAllReadNotification(isRead: Boolean) {
        fcmDao.setAllReadNotification(isRead = isRead)
    }

    override suspend fun updateCheckedNotification(isChecked: Boolean, id: Int?) {
        fcmDao.updateCheckedNotification(isChecked = isChecked, id = id)
    }

    override suspend fun setAllUncheckedNotification(isChecked: Boolean) {
        fcmDao.setAllUncheckedNotification(isChecked = isChecked)
    }

    override suspend fun deleteNotification(isChecked: Boolean) {
        fcmDao.deleteNotification(isChecked = isChecked)
    }

}