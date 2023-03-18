package com.example.rallyapp.database.entities

import androidx.room.Embedded


data class MenuWithCategory(
    @Embedded val menu: MenuEntity,
    @Embedded val category: CategoryEntity
)
