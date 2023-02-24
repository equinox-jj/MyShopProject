package com.myshopproject.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myshopproject.data.utils.Constants.FCM_TABLE

@Entity(tableName = FCM_TABLE)
data class FcmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "notification_title")
    val notificationTitle: String? = "",

    @ColumnInfo(name = "notification_body")
    val notificationBody: String? = "",

    @ColumnInfo(name = "notification_date")
    val notificationDate: String? = "",

    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false,

    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,
)
