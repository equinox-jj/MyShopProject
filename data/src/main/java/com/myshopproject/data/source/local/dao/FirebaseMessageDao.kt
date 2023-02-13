package com.myshopproject.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myshopproject.data.source.local.entity.FcmEntity
import com.myshopproject.domain.utils.Constants.FCM_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface FirebaseMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(fcmEntity: FcmEntity)

    @Query("SELECT * FROM $FCM_TABLE ORDER BY is_read, notification_date ASC")
    fun getAllNotification(): Flow<List<FcmEntity>>

    @Query("UPDATE $FCM_TABLE SET is_read = :isRead WHERE id = :id")
    suspend fun updateReadNotification(isRead: Boolean, id: Int?)

    @Query("UPDATE $FCM_TABLE SET is_read = :isRead WHERE is_checked = 1")
    suspend fun setAllReadNotification(isRead: Boolean)

    @Query("UPDATE $FCM_TABLE SET is_checked = :isChecked WHERE id = :id")
    suspend fun updateCheckedNotification(isChecked: Boolean, id: Int?)

    @Query("UPDATE $FCM_TABLE SET is_checked = :isChecked")
    suspend fun setAllUncheckedNotification(isChecked: Boolean)

    @Query("DELETE FROM $FCM_TABLE WHERE is_checked = :isChecked")
    suspend fun deleteNotification(isChecked: Boolean)
}