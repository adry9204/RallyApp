package com.example.rallyapp.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rallyapp.database.entities.CartEntity
import com.example.rallyapp.database.entities.CartWithMenuEntity

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItemToCart(cartItem: CartEntity)

    @Query("SELECT cart.*, menu.* FROM cart INNER JOIN menu ON cart.menu_id = menu.menuId")
    fun getCartWithMenu(): List<CartWithMenuEntity>

    @Query("DELETE FROM cart WHERE cart_id=:id")
    fun deleteItemFromCart(id: Int)

}