package com.myshopproject.domain.repository

import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getListProduct(query: String?): Flow<Resource<DataProductResponse>>
    fun getListProductFavorite(query: String?, userId: Int): Flow<Resource<DataProductResponse>>
}