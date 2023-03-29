package com.example.rallyapp.api_to_database

import android.content.Context
import com.example.rallyapp.api.api_helpers.CartApiHelper
import com.example.rallyapp.database.database_helper.CartDatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartApiToDatabase(context: Context) {

    private val cartApiHelper = CartApiHelper()
    private val cartDatabaseHelper = CartDatabaseHelper(context)

    fun migrateCartFromServer(userId: Int, token: String){
        cartApiHelper.getUsersCart(userId, token){
            CoroutineScope(Dispatchers.IO).launch{
                for (item in it){
                    cartDatabaseHelper.fromDataModel().addItemToCart(item)
                }
            }
        }
    }

}