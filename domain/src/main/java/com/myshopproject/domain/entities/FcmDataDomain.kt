package com.myshopproject.domain.entities

data class FcmDataDomain(
    val id: Int,
    val notificationTitle: String = "",
    val notificationBody: String = "",
    val notificationDate: String = "",
    val isRead: Boolean = false,
)
