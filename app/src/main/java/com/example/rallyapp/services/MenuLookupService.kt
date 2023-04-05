package com.example.rallyapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.rallyapp.api.network.SocketManager
import com.example.rallyapp.api_to_database.MenuApiToDatabase
import io.socket.client.Socket


class MenuLookupService: Service() {

    companion object{
        private const val TAG = "MenuLookupService"
    }

    private lateinit var mSocket: Socket
    private lateinit var menuApiToDatabase: MenuApiToDatabase

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "service has been started")

        SocketManager.setSocket()
        SocketManager.establishConnection()
        menuApiToDatabase = MenuApiToDatabase(this)

        mSocket = SocketManager.getSocket()!!

        mSocket.on("new_menu_item"){
            menuApiToDatabase.loadMenuFromServer()
        }

        mSocket.on("new_category_item"){
            menuApiToDatabase.loadCategoryAndMenuFromServer()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.closeConnection()
    }
}