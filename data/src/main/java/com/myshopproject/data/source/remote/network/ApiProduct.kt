package com.myshopproject.data.source.remote.network

import com.myshopproject.data.source.remote.dto.*
import com.myshopproject.domain.entities.UpdateRate
import com.myshopproject.domain.entities.UpdateStockProduct
import retrofit2.http.*

interface ApiProduct {

    @GET("get_list_product_paging")
    suspend fun getListProductPaging(
        @Query("search") query: String?,
        @Query("offset") offset: Int
    ): DataProductResponseDTO

//    @GET("get_list_product")
//    suspend fun getListProduct(
//        @Query("search") query: String?,
//    ): DataProductResponseDTO

    @GET("get_list_product_favorite")
    suspend fun getListProductFavorite(
        @Query("search") query: String?,
        @Query("id_user") userId: Int
    ): DataProductResponseDTO

    @GET("get_detail_product")
    suspend fun getProductDetail(
        @Query("id_product") productId: Int,
        @Query("id_user") userId: Int,
    ): DetailProductResponseDTO

    @FormUrlEncoded
    @POST("add_favorite")
    suspend fun addProductFavorite(
        @Field("id_product") productId: Int,
        @Field("id_user") userId: Int
    ): SuccessResponseDTO

    @FormUrlEncoded
    @POST("remove_favorite")
    suspend fun removeProductFavorite(
        @Field("id_product") productId: Int,
        @Field("id_user") userId: Int
    ): SuccessResponseDTO

    @POST("update-stock")
    suspend fun updateStock(
        @Body updateStock: UpdateStockProduct
    ): SuccessResponseDTO

    @PUT("update_rate/{id}")
    suspend fun updateRate(
        @Path("id") id: Int,
        @Body updateRate: UpdateRate
    ): SuccessResponseDTO
}