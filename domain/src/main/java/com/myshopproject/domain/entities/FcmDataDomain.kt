package com.myshopproject.domain.entities

data class FcmDataDomain(
    val id: Int? = null,
    val notificationTitle: String? = "",
    val notificationBody: String? = "",
    val notificationDate: String? = "",
    val isRead: Boolean = false,
    val isChecked: Boolean = false,
)
