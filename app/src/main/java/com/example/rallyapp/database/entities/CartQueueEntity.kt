package com.example.rallyapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_queue")
data class CartQueueEntity (
    @PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "id") val id: Int ?,
    @ColumnInfo(name = "cart_quantity") var quantity: Int,
    @ColumnInfo(name = "cart_userid") var userId: Int,
    @ColumnInfo(name = "menu_id") var menuId: Int
)