package com.myshopproject.data.repository

import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.source.remote.network.ApiAuth
import com.myshopproject.domain.entities.ChangeImageResponse
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiAuth: ApiAuth
) : AuthRepository {

    override fun loginAccount(email: String, password: String, firebaseToken: String): Flow<Resource<LoginResult>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiAuth.loginAccount(
                email = email,
                password = password,
                firebaseToken = firebaseToken
            ).success.toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun registerAccount(
        image: MultipartBody.Part,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiAuth.registerAccount(
                image = image,
                email = email,
                password = password,
                name = name,
                phone = phone,
                gender = gender
            ).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiAuth.changePassword(
                id = id,
                password = password,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            ).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }

    override fun changeImage(
        id: Int,
        image: MultipartBody.Part
    ): Flow<Resource<ChangeImageResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiAuth.changeImage(
                id = id,
                image = image
            ).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            when(t) {
                is HttpException -> {
                    when (t.code()) {
                        400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                        else -> emit(Resource.Error(message = null, errorCode = null, errorBody = null))
                    }
                }
                is Exception -> {
                    emit(Resource.Error(t.message, null, null))
                }
            }
        }
    }
}