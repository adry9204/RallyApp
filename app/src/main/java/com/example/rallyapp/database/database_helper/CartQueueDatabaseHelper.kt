package com.example.rallyapp.database.database_helper

import android.content.Context
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.database.RallyDatabase
import com.example.rallyapp.database.daos.CartQueueDao
import com.example.rallyapp.database.entities.CartEntity
import com.example.rallyapp.database.entities.CartQueueEntity
import com.example.rallyapp.database.entities.MenuWithCategory
import com.example.rallyapp.database.entities.entity_model_adapters.MenuWithCategoryEntityAdapter
import com.example.rallyapp.repo.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CartQueueDatabaseHelper(val context: Context) {

    companion object {
        const val TAG = "CartQueueDatabaseHelper"
    }

    private var db: RallyDatabase = RallyDatabase.getInstance(context)

    fun addToQueue(menuId: Int, quantity: Int, userId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val cartQueueItem = CartQueueEntity(
                userId = userId,
                menuId = menuId,
                quantity = quantity,
                id = null
            )
            db.cartQueueDao().addItemToCartQueue(cartQueueItem)
        }
    }

    fun getUsersCartQueue(userId: Int, callback: (cartQueue: List<CartQueueEntity>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val cartQueue = db.cartQueueDao().getItemsFormQueue(userId)
            callback(cartQueue)
        }
    }

    fun removeItemFromQueue(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            db.cartQueueDao().removeItemFromQueue(id)
        }
    }


    fun getUsersCartQueueAsModel(userId: Int, callback: (cartQueue: List<Cart>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch{
            getUsersCartQueue(userId){ cartQueueEntity ->
                val carts = mutableListOf<Cart>()
                for (item in cartQueueEntity){
                    val menuDatabaseHelper = MenuCategoryDatabaseHelper(context)
                    var cartQueue = mutableListOf<Cart>()
                    val user = User(
                        id = userId
                    )
                    val menu = db.menuWithCategoryDao().getMenuItem(item.menuId)
                    val menuWithCategoryEntityAdapter =  MenuWithCategoryEntityAdapter()
                    val menuModel = menuWithCategoryEntityAdapter.menuWithCategoryToModel(menu)
                    val cart = Cart(
                        id = item.id!!,
                        menu = menuModel,
                        user = user,
                        price = (menuModel.price.toFloat() * item.quantity).toString(),
                        quantity = item.quantity,
                        pending = true
                    )
                    carts.add(cart)
                }
                callback(carts)
            }
        }
    }

}