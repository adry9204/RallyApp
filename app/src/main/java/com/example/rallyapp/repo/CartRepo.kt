package com.example.rallyapp.repo

import android.content.Context
import com.example.rallyapp.api.api_helpers.CartApiHelper
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.database.database_helper.CartDatabaseHelper
import com.example.rallyapp.database.database_helper.CartQueueDatabaseHelper

class CartRepo(context: Context) {

    private var cartApiHelper: CartApiHelper = CartApiHelper()
    private var cartDatabaseHelper: CartDatabaseHelper
    private var cartQueueDatabaseHelper: CartQueueDatabaseHelper

    init {
        cartDatabaseHelper = CartDatabaseHelper(context)
        cartQueueDatabaseHelper = CartQueueDatabaseHelper(context)
    }

    fun getUsersCart(callback: (List<Cart>) -> Unit){
        cartDatabaseHelper.AsModel().getCartWithMenu {
            callback(it)
        }
    }

    fun addCItemToCart(
        userId: Int,
        menuId: Int,
        quantity: Int,
        authorizationToken: String,
        callback: (ApiResponse<Cart>) -> Unit
    ){
        cartApiHelper.addCItemToCart(
            userId = userId,
            menuId = menuId,
            quantity = quantity,
            authorizationToken = authorizationToken
        ){
            callback(it)
        }
    }

    fun removeFromCart(cartId: Int, token: String, callback: (ApiResponse<Cart>) -> Unit){
        cartApiHelper.removeFromCart(cartId, token){
            callback(it)
        }
    }
}