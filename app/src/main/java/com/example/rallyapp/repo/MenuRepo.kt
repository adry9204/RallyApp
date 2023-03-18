package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.api.api_helpers.MenuApiHelper
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.network.RetrofitClient
import com.example.rallyapp.api_to_database.MenuApiToDatabase
import com.example.rallyapp.database.database_helper.MenuCategoryDatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepo(context: Context) {

    private var menuApiHelper: MenuApiHelper = MenuApiHelper()
    private var menuCategoryDatabaseHelper: MenuCategoryDatabaseHelper
    private var menuApiToDatabase: MenuApiToDatabase

    init {
        menuCategoryDatabaseHelper = MenuCategoryDatabaseHelper(context)
        menuApiToDatabase = MenuApiToDatabase(context)
    }

    fun getAllMenu(callback: (List<Menu>) -> Unit){
        menuCategoryDatabaseHelper.AsDataModel().getAllMenuWithCategory {
            if(it.isNotEmpty()){
                callback(it)
            }else{
                menuApiHelper.getAllMenu{ menu ->
                    callback(menu)
                    for (item in menu){
                        menuCategoryDatabaseHelper.FromDataModel().insertMenuItem(item)
                    }
                }
            }
        }
    }

    fun searchMenu(searchString: String, callback: (List<Menu>) -> Unit){
        menuApiHelper.searchMenu(searchString){
            callback(it)
        }
    }

    fun getMenuItemById(menuId: Int, callback: (List<Menu>) -> Unit){
        menuApiHelper.getMenuItemById(menuId){
            callback(it)
        }
    }
}