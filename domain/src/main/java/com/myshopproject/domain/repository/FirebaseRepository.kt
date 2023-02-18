package com.myshopproject.domain.repository

import com.myshopproject.domain.entities.PaymentTypeResponse
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun getPaymentMethod(): Flow<Resource<List<PaymentTypeResponse>>>
}