package com.myshopproject.domain.repository

import com.myshopproject.domain.entities.DataRegister
import com.myshopproject.domain.entities.LoginResult
import com.myshopproject.domain.entities.RegisterResponse
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CreateAccountRepository {
    fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>>
    fun registerAccount(dataUser: DataRegister): Flow<Resource<RegisterResponse>>
}