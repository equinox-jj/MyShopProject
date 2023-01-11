package com.myshopproject.data.remote.network

import com.myshopproject.data.remote.dto.LoginResponseDTO
import com.myshopproject.data.remote.dto.RegisterResponseDTO
import com.myshopproject.domain.entities.DataRegister
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("ecommerce/authentication")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponseDTO

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("ecommerce/registration")
    suspend fun registerAccount(
        @Body dataUser: DataRegister
    ): RegisterResponseDTO

}