package com.myshopproject.domain.repository

import com.myshopproject.domain.entities.ChangeImageResponse
import com.myshopproject.domain.entities.DataProductResponse
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthRepository {
    fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>>
    fun registerAccount(image: MultipartBody.Part, email: RequestBody, password: RequestBody, name: RequestBody, phone: RequestBody, gender: Int): Flow<Resource<SuccessResponseStatus>>
    fun changePassword(id: Int, password: String, newPassword: String, confirmPassword: String): Flow<Resource<SuccessResponseStatus>>
    fun changeImage(id: Int, image: MultipartBody.Part): Flow<Resource<ChangeImageResponse>>
    fun getListProduct(): Flow<Resource<DataProductResponse>>
    fun getListProductFavorite(query: String, userId: Int): Flow<Resource<DataProductResponse>>
}