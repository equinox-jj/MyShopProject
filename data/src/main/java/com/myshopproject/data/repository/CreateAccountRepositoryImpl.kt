package com.myshopproject.data.repository

import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.remote.network.ApiService
import com.myshopproject.domain.entities.DataRegister
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.entities.RegisterResponse
import com.myshopproject.domain.repository.CreateAccountRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CreateAccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CreateAccountRepository {

    override fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.loginAccount(email, password).success.toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when(t.code()) {
                    400 -> emit(Resource.Error(true, t.message(), t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.message(), t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.message(), t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, null,null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(true, e.localizedMessage, null, null))
        }
    }

    override fun registerAccount(dataUser: DataRegister): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.registerAccount(dataUser).toDomain()
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            if (t is HttpException) {
                when(t.code()) {
                    400 -> emit(Resource.Error(true, t.message(), t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.message(),t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.message(),t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, null,null, null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(true, e.localizedMessage, null, null))
        }
    }
}