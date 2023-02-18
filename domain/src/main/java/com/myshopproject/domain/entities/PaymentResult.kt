package com.myshopproject.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentResult(
    val id: String,
    val name: String,
    val order: Int,
    val status: Boolean
): Parcelable