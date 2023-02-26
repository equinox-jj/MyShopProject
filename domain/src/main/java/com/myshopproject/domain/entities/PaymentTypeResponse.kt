package com.myshopproject.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentTypeResponse(
    val id: String? = null,
    val type: String? = null,
    val order: Int? = null,
    val data: List<PaymentResult>
): Parcelable
