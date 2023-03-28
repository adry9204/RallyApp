package com.example.rallyapp.database.entities

import androidx.room.Embedded


data class CartWithMenuEntity (
    @Embedded val cart: CartEntity,
    @Embedded val menu: MenuEntity,
)