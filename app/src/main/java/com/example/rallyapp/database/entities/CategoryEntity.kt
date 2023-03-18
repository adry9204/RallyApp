package com.example.rallyapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey @ColumnInfo(name = "categoryId") val categoryId: Int,
    @ColumnInfo(name = "categoryDisplayName") val categoryDisplayName: String,
)
