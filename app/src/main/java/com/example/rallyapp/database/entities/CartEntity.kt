package com.example.rallyapp.database.entities

import android.view.Menu
import android.view.MenuItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cart", foreignKeys = [ForeignKey(
    entity = MenuEntity::class,
    parentColumns = arrayOf("menuId"),
    childColumns = arrayOf("menu_id"),
    onDelete = ForeignKey.CASCADE
)])

data class CartEntity(
    @PrimaryKey @ColumnInfo(name = "cart_id") var id: Int,
    @ColumnInfo(name = "cart_quantity") var quantity: Int,
    @ColumnInfo(name = "cart_price") var price: String,
    @ColumnInfo(name = "cart_userid") var userId: Int,
    @ColumnInfo(name = "menu_id") var menuId: Int
)
