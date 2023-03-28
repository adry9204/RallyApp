package com.example.rallyapp.database.database_helper

import android.content.Context
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.database.RallyDatabase
import com.example.rallyapp.database.entities.CartQueueEntity

class CartQueueDatabaseHelper(context: Context) {

    companion object {
        const val TAG = "CartQueueDatabaseHelper"
    }

    private var db: RallyDatabase

    init {
        db = RallyDatabase.getInstance(context)
    }

    fun addToQueue(menuId: Int, quantity: Int, userId: Int){
        val cartQueueItem = CartQueueEntity(
            userId = userId,
            menuId = menuId,
            quantity = quantity,
            id = null
        )
        db.cartQueueDao().addItemToCartQueue(cartQueueItem)
    }

}