package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.CartActivity
import com.example.rallyapp.dataModel.response_models.Cart

class CartActivityViewModel: ViewModel() {

    companion object{
        private const val TAG = "CartActivityViewModel"
    }

    private var _cartListLiveData = MutableLiveData<List<Cart>>()
    var cartLiveData: MutableLiveData<List<Cart>> = _cartListLiveData

    fun getUserCart(userId: Int, authorizationToken: String){
        CartActivity.cartRepo?.let{ cartRepo ->
            cartRepo.getUsersCart(userId, authorizationToken){
                _cartListLiveData.value = it
            }

        }
    }

}