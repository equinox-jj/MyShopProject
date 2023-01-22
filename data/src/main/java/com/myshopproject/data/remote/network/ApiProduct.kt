package com.myshopproject.data.remote.network

import com.myshopproject.data.remote.dto.DataProductResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiProduct {

    @GET("get_list_product")
    suspend fun getListProduct(
        @Query("search") query: String?
    ): DataProductResponseDTO

    @GET("get_list_product_favorite")
    suspend fun getListProductFavorite(
        @Query("search") query: String?,
        @Query("id_user") userId: Int
    ): DataProductResponseDTO

}