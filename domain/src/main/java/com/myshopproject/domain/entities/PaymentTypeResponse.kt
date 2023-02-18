package com.myshopproject.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentTypeResponse(
    val id: String,
    val type: String,
    val order: Int,
    val data: List<PaymentResult>
): Parcelable
