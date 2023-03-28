package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.api.api_helpers.CategoryApiHelper
import com.example.rallyapp.api.api_helpers.MenuApiHelper
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.network.RetrofitClient
import com.example.rallyapp.api_to_database.MenuApiToDatabase
import com.example.rallyapp.database.database_helper.MenuCategoryDatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    menuCategoryDatabaseHelper.FromDataModel().insetCategoryList(category)
                }
            }
        }
    }
}