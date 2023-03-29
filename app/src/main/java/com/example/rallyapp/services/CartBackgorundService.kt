package com.example.rallyapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.api.network.SocketManager
import com.example.rallyapp.api_to_database.CartApiToDatabase
import com.example.rallyapp.database.database_helper.CartDatabaseHelper
import com.example.rallyapp.database.database_helper.MenuCategoryDatabaseHelper
import com.example.rallyapp.database.entities.CartQueueEntity
import com.example.rallyapp.repo.CartRepo
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.NotificationHelper
import com.example.rallyapp.utils.SuccessFailureCallBack
import com.google.gson.Gson
import io.socket.client.Socket

class CartBackgroundService: Service() {

    companion object{
        const val TAG = "CartBackgroundServices"

        const val CMD_KEY = "cmd_key"

        const val CMD_USER_LOGGED_IN = 10

        const val KEY_USER_ID = "user_id"
        const val KEY_AUTH_TOKEN = "auth_token"
    }

    private lateinit var mSocket: Socket
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate() {
        super.onCreate()
        handleNetworkConnectDisconnect()
    }

    private fun handleNetworkConnectDisconnect(){
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d(TAG, "Internet connected")

                if(UserCredentials.isUserSet()){
                    executeCartQueue(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(TAG, "Internet disconnected")

            }
        }

        // Register the network callback
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun executeCartQueue(userId: Int, token: String){
        val cartRepo = CartRepo(this)
        cartRepo.pushCartQueueToApi(
            userId = userId,
            token = token,
            callback = object: SuccessFailureCallBack<CartQueueEntity> {
                override fun onFailure(data: CartQueueEntity) {
                    val menuCategoryDatabaseHelper = MenuCategoryDatabaseHelper(this@CartBackgroundService)
                    menuCategoryDatabaseHelper.AsDataModel().getMenuItem(data.menuId){
                        val notificationHelper = NotificationHelper(this@CartBackgroundService)
                        notificationHelper.displaySimpleNotification("Failed", "Failed to add ${it.name} to cart")
                    }
                }

                override fun onSuccess(data: CartQueueEntity) {
                    val menuCategoryDatabaseHelper = MenuCategoryDatabaseHelper(this@CartBackgroundService)
                    menuCategoryDatabaseHelper.AsDataModel().getMenuItem(data.menuId){
                        val notificationHelper = NotificationHelper(this@CartBackgroundService)
                        notificationHelper.displaySimpleNotification("Success", "Added ${it.name} to cart")
                        migrateCartFromApiToDatabase()
                    }
                }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(TAG, "CartBackgroundService has started ${intent!!.extras!!.getInt(CMD_KEY)}")
        if(intent != null && intent.hasExtra(CMD_KEY)){
            handleCommands(intent.extras!!)
        }
        return START_STICKY
    }

    private fun handleCommands(bundle: Bundle){
        when(bundle.getInt(CMD_KEY, 0)){
            CMD_USER_LOGGED_IN -> {
                SocketManager.establishConnectionWithUser(UserCredentials.getUserId()!!)
                mSocket = SocketManager.getSocket()!!
                setSocket()
                migrateCartFromApiToDatabase()
            }
            else -> {
                Log.i(TAG, "Received unknown command in intent")
            }
        }
    }

    private fun setSocket(){
        mSocket.on("cart_updated"){
            Log.i(TAG, "cart_updated socket Triggered")
            migrateCartFromApiToDatabase()
        }

        mSocket.on("cart_delete"){
            Log.i(TAG, "cart_deleted socket Triggered")
            val gson = Gson()
            var cart = gson.fromJson(it[0].toString(), Cart::class.java)
            val cartDatabaseHelper = CartDatabaseHelper(this)
            cartDatabaseHelper.deleteItemFromCart(cart.id)
        }
    }

    private fun migrateCartFromApiToDatabase(){
        val cartApiToDatabase = CartApiToDatabase(this)
        if(!UserCredentials.isUserSet()){
            return
        }
        cartApiToDatabase.migrateCartFromServer(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.closeConnection()
    }

}