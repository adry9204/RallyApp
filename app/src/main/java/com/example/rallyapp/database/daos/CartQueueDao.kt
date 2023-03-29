package com.example.rallyapp.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rallyapp.database.entities.CartQueueEntity
import com.example.rallyapp.database.entities.CartWithMenuEntity

@Dao
interface CartQueueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemToCartQueue(cartItem: CartQueueEntity)

    @Query("SELECT * FROM cart_queue WHERE cart_userid=:userId")
    fun getItemsFormQueue(userId: Int): List<CartQueueEntity>

    @Query("DELETE FROM cart_queue WHERE id=:id")
    fun removeItemFromQueue(id: Int)
}