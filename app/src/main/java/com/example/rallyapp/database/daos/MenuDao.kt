package com.example.rallyapp.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rallyapp.database.entities.MenuEntity


@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuItems(menu: MenuEntity)

    @Query("SELECT * FROM menu WHERE menuId = :menuId")
    fun getMenuItem(menuId: Int): MenuEntity

}