package com.myshopproject.data.source.remote.network

import com.myshopproject.data.source.remote.dto.ChangeImageResponseDTO
import com.myshopproject.data.source.remote.dto.LoginResponseDTO
import com.myshopproject.data.source.remote.dto.SuccessResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiAuth {

    @FormUrlEncoded
    @POST("authentication")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("token_fcm") firebaseToken: String
    ): LoginResponseDTO

    @Multipart
    @POST("registration")
    suspend fun registerAccount(
        @Part image: MultipartBody.Part,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("gender") gender: Int,
    ): SuccessResponseDTO

    @FormUrlEncoded
    @PUT("change-password/{id}")
    suspend fun changePassword(
        @Path("id") id: Int,
        @Field("password") password: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_password") confirmPassword: String
    ): SuccessResponseDTO

    @POST("change-image")
    @Multipart
    suspend fun changeImage(
        @Part("id") id: Int,
        @Part image: MultipartBody.Part,
    ): ChangeImageResponseDTO
}