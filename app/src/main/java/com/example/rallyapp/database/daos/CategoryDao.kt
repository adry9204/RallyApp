package com.example.rallyapp.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.database.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryItem(category: CategoryEntity)

    @Query("SELECT * FROM category")
    fun getAllCategoryItems(): List<CategoryEntity>
}