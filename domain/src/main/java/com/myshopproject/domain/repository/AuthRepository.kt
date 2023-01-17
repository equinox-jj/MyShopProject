package com.myshopproject.domain.repository

import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>>
    fun registerAccount(
//        image: MultipartBody.Part,
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: Int
    ): Flow<Resource<SuccessResponseStatus>>
    fun changePassword(
//        authorization: String,
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<SuccessResponseStatus>>
}