package com.myshopproject.data.source.local.dao

import androidx.room.*
import com.myshopproject.data.source.local.entity.CartEntity
import com.myshopproject.domain.utils.Constants.CART_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCartDao {

    @Query("SELECT * FROM $CART_TABLE")
    fun getAllProduct(): Flow<List<CartEntity>>

    @Query("SELECT * FROM $CART_TABLE WHERE is_checked = '1'")
    fun getAllCheckedProduct(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductToTrolley(trolley: CartEntity)

    @Query("UPDATE $CART_TABLE SET quantity = :quantity, item_total_price = :itemTotalPrice WHERE id = :id")
    suspend fun updateProductData(quantity: Int?, itemTotalPrice: Int?, id: Int?)

    @Query("UPDATE $CART_TABLE SET is_checked = :isChecked")
    suspend fun updateProductIsCheckedAll(isChecked: Boolean)

    @Query("UPDATE $CART_TABLE SET is_checked = :isChecked WHERE id = :id")
    suspend fun updateProductIsCheckedById(isChecked: Boolean, id: Int?)

    @Query("DELETE FROM $CART_TABLE WHERE id = :id")
    suspend fun deleteProductByIdFromTrolley(id: Int?)
}