package com.example.rallyapp.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.rallyapp.database.entities.MenuEntity
import com.example.rallyapp.database.entities.MenuWithCategory

@Dao
interface MenuWithCategoryDao {

    @Query("SELECT menu.*, category.* FROM menu INNER JOIN category ON menu.menuCategoryId = category.categoryId")
    fun getAllMenuWithCategory(): List<MenuWithCategory>

    @Query("SELECT menu.*, category.* FROM menu INNER JOIN category ON menu.menuCategoryId = category.categoryId WHERE menuId = :menuId")
    fun getMenuItem(menuId: Int): MenuWithCategory
}