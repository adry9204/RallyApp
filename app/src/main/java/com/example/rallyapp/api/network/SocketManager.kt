package com.example.rallyapp.api.network

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketManager {

    private var mSocket: Socket? = null
    var socketUser = mutableListOf<String>()
    var server = ServerConfigs.HEROKU_SERVER

    @Synchronized
    fun setSocket() {
        if(mSocket == null){
            try {
//                mSocket = IO.socket("https://still-brushlands-66800.herokuapp.com")
                mSocket = IO.socket(server)
            } catch (e: URISyntaxException) {
                print(e.printStackTrace())
            }
        }
    }

    @Synchronized
    fun getSocket(): Socket? {
        socketUser.add("user")
        return mSocket
    }

    @Synchronized
    fun establishConnectionWithUser(userId: Int){
        mSocket?.let {
            it.disconnect()
        }
        val options = IO.Options()
        options.query = "userId=$userId"
        mSocket = IO.socket(server, options)
        mSocket?.let {
            it.connect()
        }
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.let {
            it.connect()
        }
    }

    @Synchronized
    fun closeConnection() {
        socketUser.removeLast()
        if(socketUser.isEmpty()) {
            mSocket?.let {
                it.disconnect()
            }
        }
    }
}