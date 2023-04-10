package com.example.rallyapp.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.api.network.SocketManager
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.NotificationHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Socket
import java.lang.reflect.Type

class OrderBackgroundService: Service() {

    companion object{
        const val CMD_KEY = "cmd_key"
        const val CMD_USER_LOGGED_IN = 10
    }

    private lateinit var mSocket: Socket
    private lateinit var socketManager: SocketManager
    private lateinit var notificationHelper: NotificationHelper

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationHelper = NotificationHelper(this)
        socketManager = SocketManager()
        Log.i(
            CartBackgroundService.TAG, "OrderBackgroundService has started ${intent!!.extras!!.getInt(
                CartBackgroundService.CMD_KEY
            )}")

        if(intent != null && intent.hasExtra(CartBackgroundService.CMD_KEY)){
            handleCommands(intent.extras!!)
        }
        return START_STICKY
    }

    private fun handleCommands(bundle: Bundle){
        when(bundle.getInt(CMD_KEY, 0)){
            CMD_USER_LOGGED_IN -> {
                socketManager.establishConnectionWithUser(UserCredentials.getUserId()!!)
                mSocket = socketManager.getSocket()!!
                setSocket()
            }
            else -> {
                Log.i(CartBackgroundService.TAG, "Received unknown command in intent")
            }
        }
    }

    private fun setSocket(){
        mSocket.on("order_status_change"){
            Log.i(CartBackgroundService.TAG, "order_status_changed socket triggered")
            val gson = Gson()
            class Token : TypeToken<Order<User>>()
            val order = gson.fromJson<Order<User>>(it[0].toString(), Token().type)
            notificationHelper.displaySimpleNotification(
                title = "Order Status updated",
                message = "You order is ${order.status}"
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.closeConnection()
    }
}