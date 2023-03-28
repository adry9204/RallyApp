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

    @Query("SELECT * FROM cart_queue")
    fun getItemsFormQueue(): List<CartQueueEntity>
}