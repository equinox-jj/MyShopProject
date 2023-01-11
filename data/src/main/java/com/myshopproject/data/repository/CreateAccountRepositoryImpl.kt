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
import javax.inject.Inject

class CreateAccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CreateAccountRepository {

    override fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>> = flow {
        emit(Resource.Loading())
        try {
            val result = apiService.loginAccount(email, password).success.toDomain()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred."))
        }
    }

    override fun registerAccount(dataUser: DataRegister): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading())
        try {
            val result = apiService.registerAccount(dataUser).toDomain()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred."))
        }
    }
}