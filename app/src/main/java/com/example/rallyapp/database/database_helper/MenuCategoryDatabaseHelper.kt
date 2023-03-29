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
                    menuCategoryId = menu.category!!.id,
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

        fun insertCategoryList(category: List<Category>){
            CoroutineScope(Dispatchers.IO).launch {
                for(item in category){
                    insertCategoryItem(item)
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

        fun getAllCategoryItems(callback: ((categoryItems: List<Category>) -> Unit)? = null){
            CoroutineScope(Dispatchers.IO).launch {
                val categoryEntity = db.categoryDao().getAllCategoryItems()
                val categoryModel = mutableListOf<Category>()

                for(item in categoryEntity){
                    val categoryItemModel = Category(
                        category = item.categoryDisplayName,
                        id = item.categoryId
                    )
                    categoryModel.add(categoryItemModel)
                }

                callback?.let {
                    withContext(Dispatchers.Main){
                        it(categoryModel.toList())
                    }
                }
            }
        }

        fun getMenuItem(menuId: Int, callback: (menuItem: Menu) -> Unit){
            CoroutineScope(Dispatchers.IO).launch {
                val menuItem = db.menuDao().getMenuItem(menuId = menuId)
                val menu = Menu(
                    id = menuItem.menuId,
                    description = menuItem.description,
                    image = menuItem.image,
                    name = menuItem.name,
                    price = menuItem.price
                )
                callback(menu)
            }
        }

        fun getMenuItemWithCategory(menuId: Int, callback: (menuItem: Menu) -> Unit){
            CoroutineScope(Dispatchers.IO).launch {
                val menuItem = db.menuWithCategoryDao().getMenuItem(menuId = menuId)
                val category = Category(
                    category = menuItem.category.categoryDisplayName,
                    id = menuItem.category.categoryId
                )
                val menu = Menu(
                    id = menuItem.menu.menuId,
                    description = menuItem.menu.description,
                    image = menuItem.menu.image,
                    name = menuItem.menu.name,
                    price = menuItem.menu.price,
                    category = category
                )
                withContext(Dispatchers.Main) {
                    callback(menu)
                }
            }
        }

        fun searchMenuItem(searchTerm: String, callback: (List<Menu>) -> Unit){
            CoroutineScope(Dispatchers.IO).launch {
                val pattern = "%${searchTerm.lowercase()}%"
                Log.i(TAG, "searchMenuItem -> $pattern")
                val menuItems = db.menuWithCategoryDao().searchMenuItem(pattern)
                val menuModels = mutableListOf<Menu>()
                for (menuItem in menuItems){
                    val category = Category(
                        category = menuItem.category.categoryDisplayName,
                        id = menuItem.category.categoryId
                    )
                    val menu = Menu(
                        id = menuItem.menu.menuId,
                        description = menuItem.menu.description,
                        image = menuItem.menu.image,
                        name = menuItem.menu.name,
                        price = menuItem.menu.price,
                        category = category
                    )
                    menuModels.add(menu)
                }
                withContext(Dispatchers.Main){
                    Log.i(TAG, menuModels.toString())
                    callback(menuModels.toList())
                }
            }
        }
    }
}