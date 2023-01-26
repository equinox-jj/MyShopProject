package com.myshopproject.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.utils.Constants.CART_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Query("DELETE FROM $CART_TABLE WHERE id = :id")
    suspend fun deleteCart(id: Int)

    @Query("SELECT * FROM $CART_TABLE ORDER BY id ASC")
    fun getAllCarts(): Flow<List<CartEntity>>

}