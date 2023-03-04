package com.myshopproject.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentResult(
    val id: String? = null,
    val name: String? = null,
    val order: Int? = null,
    val status: Boolean = false
): Parcelable