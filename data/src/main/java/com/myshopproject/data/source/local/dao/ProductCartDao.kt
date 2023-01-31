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

    @Query("UPDATE $CART_TABLE SET quantity_cart = :q, total_price_cart = :totalPriceCart WHERE id = :id")
    suspend fun updateCartData(q: Int?, totalPriceCart: Int?, id: Int?)

    @Query("UPDATE $CART_TABLE SET is_checked_cart = :isChecked")
    suspend fun updateCartIsCheckedAll(isChecked: Boolean)

    @Query("UPDATE $CART_TABLE SET is_checked_cart = :isChecked WHERE id = :id")
    suspend fun updateCartIsCheckedById(isChecked: Boolean, id: Int?)

    @Query("SELECT * FROM $CART_TABLE ORDER BY id ASC")
    fun getAllCarts(): Flow<List<CartEntity>>

    @Query("SELECT * FROM $CART_TABLE WHERE is_checked_cart = 1")
    fun getCartIsChecked(): Flow<List<CartEntity>>

    @Query("SELECT * FROM $CART_TABLE WHERE id = :id")
    fun getCartById(id: Int): Flow<List<CartEntity>>
}