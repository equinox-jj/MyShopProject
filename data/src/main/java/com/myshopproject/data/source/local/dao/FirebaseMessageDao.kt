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

    @Query("SELECT * FROM $FCM_TABLE ORDER BY id ASC")
    fun getAllNotification(): Flow<List<FcmEntity>>

    @Query("SELECT COUNT(*) FROM $FCM_TABLE")
    suspend fun notificationCount(total: Int)
}