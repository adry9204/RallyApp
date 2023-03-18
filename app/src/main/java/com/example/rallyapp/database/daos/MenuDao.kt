package com.example.rallyapp.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.rallyapp.database.entities.MenuEntity
import retrofit2.http.Query

@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuItems(menu: MenuEntity)

}