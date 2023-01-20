package com.myshopproject.data.repository

import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.remote.network.ApiService
import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepository {
    override fun getListProduct(query: String?): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getListProduct(query).toDomain()
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
        query: String,
        userId: Int
    ): Flow<Resource<DataProductResponse>> = flow {
        emit(Resource.Loading)
        try {

        } catch (t: Throwable) {

        }
    }
}