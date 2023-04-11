package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.api.api_helpers.CartApiHelper
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.database.database_helper.CartDatabaseHelper
import com.example.rallyapp.database.database_helper.CartQueueDatabaseHelper
import com.example.rallyapp.database.entities.CartQueueEntity
import com.example.rallyapp.utils.NetworkHandleCallback
import com.example.rallyapp.utils.NetworkHelper
import com.example.rallyapp.utils.SuccessFailureCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRepo(private var context: Context) {

    private var cartApiHelper: CartApiHelper = CartApiHelper()
    private var cartDatabaseHelper: CartDatabaseHelper = CartDatabaseHelper(context)
    private var cartQueueDatabaseHelper: CartQueueDatabaseHelper = CartQueueDatabaseHelper(context)

    companion object{
        const val TAG = "CartRepo"
    }

    fun getUsersCart(userId: Int, callback: (List<Cart>) -> Unit){
        cartDatabaseHelper.AsModel().getCartWithMenu(userId) {
            callback(it)
        }
    }

    fun getUsersCartAndInQueue(userId: Int, callback: (List<Cart>) -> Unit){
        getUsersCart(userId) { cartItems ->
            cartQueueDatabaseHelper.getUsersCartQueueAsModel(userId){ cartQueue ->
                CoroutineScope(Dispatchers.IO).launch{
                    Log.i(TAG, "Inside the getUsersCartQueueAsModel")
                    var carts = cartItems.toMutableList()
                    carts.addAll(cartQueue)
                    withContext(Dispatchers.Main){
                        callback(carts)
                    }
                }
            }
        }
    }

    fun addItemToCart(
        userId: Int,
        menuId: Int,
        quantity: Int,
        authorizationToken: String,
        callback: NetworkHandleCallback<ApiResponse<Cart>>
    ){
        val networkHelper = NetworkHelper(context)
        if(networkHelper.isConnectedToNetwork()){
            cartApiHelper.addItemToCart(
                userId = userId,
                menuId = menuId,
                quantity = quantity,
                authorizationToken = authorizationToken
            ){
                callback.onConnected(it)
            }
        }else{
            callback.onDisconnected()
            cartQueueDatabaseHelper.addToQueue(
                menuId = menuId,
                userId = userId,
                quantity = quantity
            )
            Log.e(TAG, "addItemToCart -> Not Connected to the network")
        }
    }

    fun removeFromCart(cartId: Int, token: String, callback: (ApiResponse<Cart>) -> Unit){
        cartApiHelper.removeFromCart(cartId, token){
            callback(it)
        }
    }

    fun pushCartQueueToApi(userId: Int, token: String, callback: SuccessFailureCallBack<CartQueueEntity>){
        cartQueueDatabaseHelper.getUsersCartQueue(userId){ cartQueue ->
            CoroutineScope(Dispatchers.IO).launch {
                for (item in cartQueue) {
                    cartApiHelper.addItemToCart(
                        userId = item.userId,
                        menuId = item.menuId,
                        quantity = item.quantity,
                        authorizationToken = token
                    ) { response ->
                        if (response.success == 1) {
                            cartQueueDatabaseHelper.removeItemFromQueue(item.id!!)
                            callback.onSuccess(item)
                        }else{
                            callback.onFailure(item)
                        }
                    }
                }
            }
        }
    }

    fun updateCartQuantity(
        cartId: Int,
        quantity: Int,
        token: String,
        callback: NetworkHandleCallback<ApiResponse<Cart>>
    ){
        val networkHelper = NetworkHelper(context)
        if(networkHelper.isConnectedToNetwork()){
            cartApiHelper.updateCartItemQuantity(cartId, quantity, token){
                callback.onConnected(it)
            }
        }else{
            callback.onDisconnected()
        }
    }
}