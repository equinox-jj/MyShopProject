package com.myshopproject.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myshopproject.domain.utils.Constants

@Entity(tableName = Constants.FCM_TABLE)
data class FcmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "notification_title")
    val notificationTitle: String = "",

    @ColumnInfo(name = "notification_body")
    val notificationBody: String = "",

    @ColumnInfo(name = "notification_date")
    val notificationDate: String = "",

    @ColumnInfo(name = "isRead")
    val isRead: Boolean = false,
)