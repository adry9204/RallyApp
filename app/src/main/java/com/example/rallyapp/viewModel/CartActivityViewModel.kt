package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.CartActivity
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartActivityViewModel: ViewModel() {

    companion object{
        private const val TAG = "CartActivityViewModel"
    }

    private var _cartListLiveData = MutableLiveData<List<Cart>>()
    val cartLiveData: MutableLiveData<List<Cart>> = _cartListLiveData

    private var _cartResponseLiveData = MutableLiveData<ApiResponse<Cart>>()
    val cartResponseLiveData: MutableLiveData<ApiResponse<Cart>> = _cartResponseLiveData

    fun getUserCart(userId: Int, authorizationToken: String){
        CartActivity.cartRepo?.let{ cartRepo ->
            cartRepo.getUsersCart{
                _cartListLiveData.value = it
            }
        }
    }

    fun removeCartItem(cartId: Int, authorizationToken: String){
        CartActivity.cartRepo?.let { cartRepo ->
            cartRepo.removeFromCart(cartId, authorizationToken){
                _cartResponseLiveData.value = it
            }
        }
    }

}