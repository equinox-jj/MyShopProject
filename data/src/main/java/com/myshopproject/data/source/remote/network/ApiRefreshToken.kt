package com.myshopproject.data.source.remote.network

import com.myshopproject.data.source.remote.dto.RefreshTokenResponseDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiRefreshToken {

    @FormUrlEncoded
    @Headers("apikey: TuIBt77u7tZHi8n7WqUC")
    @POST("refresh-interceptor")
    suspend fun getRefreshToken(
        @Field("id_user") id: Int?,
        @Field("access_token") accessToken: String?,
        @Field("refresh_token") refreshToken: String?
    ): Response<RefreshTokenResponseDTO>

}