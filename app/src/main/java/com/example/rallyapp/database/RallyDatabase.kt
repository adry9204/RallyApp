package com.example.rallyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rallyapp.database.daos.*
import com.example.rallyapp.database.entities.*

@Database(entities = [MenuEntity::class, CategoryEntity::class, CartEntity::class, CartQueueEntity::class], version = 1)
abstract class RallyDatabase: RoomDatabase() {

    abstract fun menuDao(): MenuDao
    abstract fun categoryDao(): CategoryDao
    abstract fun menuWithCategoryDao(): MenuWithCategoryDao
    abstract fun cartDao(): CartDao
    abstract fun cartQueueDao(): CartQueueDao

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