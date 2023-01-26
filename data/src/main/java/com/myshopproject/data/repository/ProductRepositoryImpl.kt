package com.myshopproject.data.repository

import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.source.local.dao.ProductCartDao
import com.myshopproject.data.source.remote.network.ApiProduct
import com.myshopproject.domain.entities.*
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiProduct: ApiProduct,
    private val cartDao: ProductCartDao
) : ProductRepository {
    override fun getListProduct(query: String?): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getListProduct(query).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    404 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        }
    }

    override fun getListProductFavorite(
        query: String?,
        userId: Int
    ): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getListProductFavorite(query, userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    404 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        }
    }

    override fun getProductDetail(productId: Int, userId: Int): Flow<Resource<DetailProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.getProductDetail(productId, userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    else -> emit(Resource.Error(null, null, null))
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
            val response = apiProduct.addProductFavorite(productId, userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    else -> emit(Resource.Error(null, null, null))
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
            val response = apiProduct.removeProductFavorite(productId, userId).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        }
    }

    override fun updateStock(updateStock: UpdateStockProduct): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.updateStock(updateStock).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        }
    }

    override fun updateRate(
        id: Int,
        updateRate: UpdateRate
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiProduct.updateRate(id, updateRate).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> { emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody())) }
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        }
    }

    override suspend fun insertCart(cartEntity: CartEntity) {
        cartDao.insertCart(cartEntity)
    }

    override suspend fun deleteCart(id: Int) {
        cartDao.deleteCart(id)
    }

    override fun getAllCarts(): Flow<List<CartEntity>> {
        return cartDao.getAllCarts()
    }
}