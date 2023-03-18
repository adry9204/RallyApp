package com.example.rallyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rallyapp.database.daos.CategoryDao
import com.example.rallyapp.database.daos.MenuDao
import com.example.rallyapp.database.daos.MenuWithCategoryDao
import com.example.rallyapp.database.entities.CategoryEntity
import com.example.rallyapp.database.entities.MenuEntity
import com.example.rallyapp.database.entities.MenuWithCategory

@Database(entities = [MenuEntity::class, CategoryEntity::class], version = 1)
abstract class RallyDatabase: RoomDatabase() {

    abstract fun menuDao(): MenuDao
    abstract fun categoryDao(): CategoryDao
    abstract fun menuWithCategoryDao(): MenuWithCategoryDao

    companion object {
        private const val DB_NAME = "rally_database"

        private var instance: RallyDatabase? = null

        fun getInstance(context: Context): RallyDatabase{
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RallyDatabase::class.java,
                    DB_NAME
                ).build().also {
                    instance = it
                }
            }
        }
    }
}