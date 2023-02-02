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
    private val cartDao: ProductCartDao,
//    private val remotePagingSource: RemotePagingSource
) : ProductRepository {

//    override fun getListProductPaging(): Flow<PagingData<DataProduct>> {
//        return Pager(
//            config = PagingConfig(pageSize = 21),
//            pagingSourceFactory = { remotePagingSource }
//        ).flow
//    }

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

    override fun getAllProduct(): Flow<List<CartEntity>> {
        return cartDao.getAllProduct()
    }

    override fun getAllCheckedProduct(): Flow<List<CartEntity>> {
        return cartDao.getAllCheckedProduct()
    }

    override fun getProductById(id: Int?): Flow<List<CartEntity>> {
        return cartDao.getProductById(id)
    }

    override suspend fun addProductToTrolley(trolley: CartEntity) {
        cartDao.addProductToTrolley(trolley)
    }

    override suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?) {
        cartDao.updateProductData(quantity, itemTotalPrice, id)
    }

    override suspend fun updateProductIsCheckedAll(isChecked: Boolean) {
        cartDao.updateProductIsCheckedAll(isChecked)
    }

    override suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?) {
        cartDao.updateProductIsCheckedById(isChecked, id)
    }

    override suspend fun deleteProductByIdFromTrolley(id: Int?) {
        cartDao.deleteProductByIdFromTrolley(id)
    }

    override suspend fun deleteAllProductFromTrolley(trolley: CartEntity) {
        cartDao.deleteAllProductFromTrolley(trolley)
    }

}