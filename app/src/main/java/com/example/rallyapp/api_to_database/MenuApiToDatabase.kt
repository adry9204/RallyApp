package com.example.rallyapp.api_to_database

import android.content.Context
import android.util.Log
import com.example.rallyapp.AppInit
import com.example.rallyapp.api.api_helpers.CategoryApiHelper
import com.example.rallyapp.api.api_helpers.MenuApiHelper
import com.example.rallyapp.database.database_helper.MenuCategoryDatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuApiToDatabase(private var context: Context) {

     fun loadMenuFromServer(){
        val menuApiHelper = MenuApiHelper()
        val menuCategoryDbHelper = MenuCategoryDatabaseHelper(context)
        menuApiHelper.getAllMenu {
            for (menuItem in it){
                menuCategoryDbHelper.FromDataModel().insertMenuItem(menuItem)
            }
        }
    }

    fun loadCategoryFromServer(){
        CoroutineScope(Dispatchers.IO).launch{
            val categoryApiHelper = CategoryApiHelper()
            val menuCategoryDatabaseHelper = MenuCategoryDatabaseHelper(context)

            categoryApiHelper.getAllCategory {
                Log.i(AppInit.TAG, it.toString());
                for (categoryItem in it){
                    menuCategoryDatabaseHelper.FromDataModel().insertCategoryItem(categoryItem)
                }
            }
        }
    }
}