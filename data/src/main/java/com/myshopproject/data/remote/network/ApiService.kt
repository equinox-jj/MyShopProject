package com.myshopproject.data.remote.network

import com.myshopproject.data.remote.dto.LoginResponseDTO
import com.myshopproject.data.remote.dto.RegisterResponseDTO
import com.myshopproject.data.remote.dto.SuccessResponseDTO
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
    @Multipart
    suspend fun registerAccount(
//        @Part image: MultipartBody.Part,
        @Part("email") email: String,
        @Part("password") password: String,
        @Part("name") name: String,
        @Part("phone") phone: String,
        @Part("gender") gender: Int,
    ): RegisterResponseDTO

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("api/ecommerce/refresh-token")
    suspend fun getRefreshToken()

    @FormUrlEncoded
    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @PUT("api/ecommerce/change-password/{id}")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Field("password") password: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_password") confirmPassword: String
    ): SuccessResponseDTO

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("api/ecommerce/registration")
    @Multipart
    suspend fun changeImage(
//        @Part image: MultipartBody.Part,
    ): RegisterResponseDTO

}