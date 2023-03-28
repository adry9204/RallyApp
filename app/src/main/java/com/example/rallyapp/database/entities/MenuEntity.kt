package com.example.rallyapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "menu", foreignKeys = [ForeignKey(
    entity = CategoryEntity::class,
    parentColumns = arrayOf("categoryId"),
    childColumns = arrayOf("menuCategoryId"),
    onDelete = ForeignKey.CASCADE
)])

data class MenuEntity(
    @ColumnInfo(name = "menuCategoryId") val menuCategoryId: Int,
    @ColumnInfo(name = "description") val description: String,
    @PrimaryKey @ColumnInfo(name = "menuId") val menuId: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: String,
)
