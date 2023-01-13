package com.myshopproject.data.remote.network

import com.myshopproject.data.remote.dto.LoginResponseDTO
import com.myshopproject.data.remote.dto.RegisterResponseDTO
import com.myshopproject.domain.entities.DataRegister
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("api/ecommerce/authentication")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponseDTO

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("api/ecommerce/registration")
    suspend fun registerAccount(
        @Body dataUser: DataRegister
    ): RegisterResponseDTO

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @GET("api/ecommerce/refresh-token")
    suspend fun getRefreshToken()

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @PUT("api/ecommerce/change-password/{id_user}")
    suspend fun changePassword(
        @Path("id") userId: Int,
    )
}