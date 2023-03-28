package com.example.rallyapp.services

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.rallyapp.api.network.SocketManager
import com.example.rallyapp.api_to_database.CartApiToDatabase
import com.example.rallyapp.user.UserCredentials
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

    override fun onCreate() {
        super.onCreate()
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

}