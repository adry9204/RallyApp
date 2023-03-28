package com.example.rallyapp.database.database_helper

import android.content.Context
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.database.RallyDatabase
import com.example.rallyapp.database.entities.CartEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartDatabaseHelper(context: Context) {
    companion object {
        const val TAG = "MenuCategoryDatabaseHelper"
    }

    private var db: RallyDatabase

    init {
        db = RallyDatabase.getInstance(context)
    }

    inner class fromDataModel{

        fun addItemToCart(cart: Cart){
            CoroutineScope(Dispatchers.IO).launch {
                val userId: Int
                val cartEntity = CartEntity(
                    id = cart.id,
                    menuId = cart.menu.id,
                    quantity = cart.quantity,
                    price = cart.price,
                    userId = cart.user.id
                )
                db.cartDao().addItemToCart(cartEntity)
            }
        }
    }

    inner class AsModel{
        fun getCartWithMenu(callback: (cartItems: List<Cart>) -> Unit){
            CoroutineScope(Dispatchers.IO).launch {
                val cartWithMenuEntity = db.cartDao().getCartWithMenu()
                val cartDataModel = mutableListOf<Cart>()
                for(item in cartWithMenuEntity){
                    val menu = Menu(
                        id = item.menu.menuId,
                        name = item.menu.name,
                        image =  item.menu.image,
                        description = item.menu.description,
                        price = item.menu.price
                    )
                    val user = User(id = item.cart.userId)
                    val cart = Cart(
                        id = item.cart.id,
                        quantity = item.cart.quantity,
                        price = item.cart.price,
                        user = user,
                        menu = menu
                    )
                    cartDataModel.add(cart)
                }
                withContext(Dispatchers.Main){
                    callback(cartDataModel.toList())
                }
            }
        }
    }
}