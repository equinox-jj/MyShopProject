package com.myshopproject.data.repository

import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.remote.network.ApiService
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.entities.SuccessResponseStatus
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.loginAccount(
                email = email,
                password = password
            ).success.toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null, null))
        }
    }

    override fun registerAccount(
//        image: MultipartBody.Part,
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: Int
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.registerAccount(
//                image = image,
                email = email,
                password = password,
                name = name,
                phone = phone,
                gender = gender
            ).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null, null))
        }
    }

    override fun changePassword(
//        authorization: String,
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<SuccessResponseStatus>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.changePassword(
//                authorization = authorization,
                id = id,
                password = password,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            ).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when(t.code()) {
                    400 -> {emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))}
                    404 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(t.message(), t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(null, null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage, null, null))
        }
    }
}