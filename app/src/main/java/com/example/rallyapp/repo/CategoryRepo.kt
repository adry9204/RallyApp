package com.example.rallyapp.repo

import android.content.Context
import com.example.rallyapp.api.api_helpers.CategoryApiHelper
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.api_to_database.MenuApiToDatabase
import com.example.rallyapp.database.database_helper.MenuCategoryDatabaseHelper

class CategoryRepo(context: Context) {

    private var categoryApiHelper: CategoryApiHelper = CategoryApiHelper()
    private var menuCategoryDatabaseHelper: MenuCategoryDatabaseHelper
    private var menuApiToDatabase: MenuApiToDatabase

    init {
        menuCategoryDatabaseHelper = MenuCategoryDatabaseHelper(context)
        menuApiToDatabase = MenuApiToDatabase(context)
    }

    fun getAllCategory(callback: (List<Category>) -> Unit){
        menuCategoryDatabaseHelper.AsDataModel().getAllCategoryItems {
            if (it.isNotEmpty()) {
                callback(it)
            } else {
                categoryApiHelper.getAllCategory { category ->
                    callback(category)
                    menuCategoryDatabaseHelper.FromDataModel().insertCategoryList(category)
                }
            }
        }
    }
}