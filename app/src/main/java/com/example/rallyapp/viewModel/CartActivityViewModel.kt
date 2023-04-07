package com.example.rallyapp.viewModel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rallyapp.activities.CartActivity
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.repo.OrderRepo
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.NetworkHandleCallback
import com.example.rallyapp.utils.NetworkHelper

class CartActivityViewModel(application: Application): AndroidViewModel(application) {

    companion object{
        private const val TAG = "CartActivityViewModel"
    }

    private val orderRepo = OrderRepo()
    private val networkHelper = NetworkHelper(getApplication<Application>().applicationContext)

    private var _cartListLiveData = MutableLiveData<List<Cart>>()
    val cartLiveData: MutableLiveData<List<Cart>> = _cartListLiveData

    private var _cartResponseLiveData = MutableLiveData<ApiResponse<Cart>>()
    val cartResponseLiveData: MutableLiveData<ApiResponse<Cart>> = _cartResponseLiveData

    private var _updateQuantityResponse = MutableLiveData<ApiResponse<Cart>>()
    val updateQuantityResponse: MutableLiveData<ApiResponse<Cart>> = _updateQuantityResponse

    private var _makeOrderFromCartResponse = MutableLiveData<ApiResponse<Order<Int>>>()
    val makeOrderFromCartResponse: MutableLiveData<ApiResponse<Order<Int>>> = _makeOrderFromCartResponse

    private val _showAlert = MutableLiveData<AlertData>()
    val showAlert: LiveData<AlertData> = _showAlert

    fun showAlert(message: String){
        val alertData = AlertData(
            title = "No network",
            message = message
        )
        _showAlert.value = alertData
    }

    fun getUserCart(userId: Int, authorizationToken: String){
        CartActivity.cartRepo?.let{ cartRepo ->
            cartRepo.getUsersCartAndInQueue(userId){
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

    fun updateUserCart(cartId: Int, quantity: Int, authorizationToken: String){
        CartActivity.cartRepo?.let { cartRepo ->
            cartRepo.updateCartQuantity(cartId, quantity, authorizationToken, object: NetworkHandleCallback<ApiResponse<Cart>>{
                override fun onConnected(data: ApiResponse<Cart>) {
                    _updateQuantityResponse.value = data
                }
                override fun onDisconnected() {
                    showAlert("Cannot perform this action without being connected ot the internet")
                }
            })
        }
    }

    fun makeOrderFromCart(userId: Int, token: String){
        if(networkHelper.isConnectedToNetwork()){
            orderRepo.makeOrderFromCart(userId, token){
                _makeOrderFromCartResponse.value = it
            }
        }else{
            _showAlert.value = AlertData(
                title = "No network",
                message = "Please connect to the internet before you checkout"
            )
        }
    }

}