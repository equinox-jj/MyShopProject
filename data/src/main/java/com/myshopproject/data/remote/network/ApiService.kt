package com.myshopproject.data.remote.network

import com.myshopproject.data.remote.dto.ChangeImageResponseDTO
import com.myshopproject.data.remote.dto.LoginResponseDTO
import com.myshopproject.data.remote.dto.SuccessResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("api/ecommerce/authentication")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponseDTO

    @Multipart
    @POST("api/ecommerce/registration")
    suspend fun registerAccount(
        @Part image: MultipartBody.Part,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("gender") gender: Int,
    ): SuccessResponseDTO

    @FormUrlEncoded
    @PUT("api/ecommerce/change-password/{id}")
    suspend fun changePassword(
        @Path("id") id: Int,
        @Field("password") password: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_password") confirmPassword: String
    ): SuccessResponseDTO


    @POST("api/ecommerce/change-image")
    @Multipart
    suspend fun changeImage(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part,
    ): ChangeImageResponseDTO

}