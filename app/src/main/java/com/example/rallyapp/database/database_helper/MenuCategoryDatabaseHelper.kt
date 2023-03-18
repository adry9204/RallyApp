package com.example.rallyapp.database.database_helper

import android.content.Context
import android.util.Log
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.database.RallyDatabase
import com.example.rallyapp.database.entities.CategoryEntity
import com.example.rallyapp.database.entities.MenuEntity
import kotlinx.coroutines.*

class MenuCategoryDatabaseHelper(context: Context) {

    companion object {
        const val TAG = "MenuCategoryDatabaseHelper"
    }

    private var db: RallyDatabase

    init {
        db = RallyDatabase.getInstance(context)
    }

    inner class FromDataModel {
        fun insertMenuItem(menu: Menu, callback: (() -> Unit)? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val menuEntity = MenuEntity(
                    menuId = menu.id,
                    name = menu.name,
                    description = menu.description,
                    menuCategoryId = menu.category.id,
                    image = menu.image,
                    price = menu.price
                )
                db.menuDao().insertMenuItems(menuEntity)
                if(callback != null){
                    callback()
                }
            }
        }

        fun insertCategoryItem(category: Category, callback: (() -> Unit)? = null) {
            Log.i(TAG, category.toString())
            CoroutineScope(Dispatchers.IO).launch{
                val categoryEntity = CategoryEntity(
                    categoryDisplayName = category.category,
                    categoryId = category.id
                )
                db.categoryDao().insertCategoryItem(categoryEntity)
                Log.i(TAG, "after inset call")
                if (callback != null) {
                    callback()
                }
            }
        }
    }

    inner class AsDataModel{

        fun getAllMenuWithCategory(callback: ((menuItems: List<Menu>) -> Unit)? = null){
            CoroutineScope(Dispatchers.IO).launch {
                val menuWithCategoryEntity = db.menuWithCategoryDao().getAllMenuWithCategory()
                val menuWithCategoryModel = mutableListOf<Menu>()
                for (item in menuWithCategoryEntity){
                    val itemCategoryModel = Category(
                        category = item.category.categoryDisplayName,
                        id = item.category.categoryId
                    )
                    val  itemMenuModel = Menu(
                        name = item.menu.name,
                        category = itemCategoryModel,
                        price = item.menu.price,
                        description = item.menu.description,
                        image = item.menu.image,
                        id = item.menu.menuId
                    )
                    menuWithCategoryModel.add(itemMenuModel)
                }

                callback?.let {
                    withContext(Dispatchers.Main){
                        it(menuWithCategoryModel.toList())
                    }
                }
            }
        }
    }
}