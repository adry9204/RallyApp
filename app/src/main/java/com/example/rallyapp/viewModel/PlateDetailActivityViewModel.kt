package com.example.rallyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.activities.PlateDetailActivity
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.NetworkHandleCallback

class PlateDetailActivityViewModel: ViewModel() {

    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    val menuListLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    private var _addCartResponseLiveData = MutableLiveData<ApiResponse<Cart>>()
    val addCartResponseLiveData: MutableLiveData<ApiResponse<Cart>> = _addCartResponseLiveData

    private val _showAlert = MutableLiveData<AlertData>()
    val showAlert: LiveData<AlertData> = _showAlert

    fun getMenuItemById(menuId: Int){
        PlateDetailActivity.menuRepo?.let { menuRepo ->
            menuRepo.getMenuItemById(menuId){
                _menuListLiveData.value = it
            }
        }
    }

    fun showAlert(message: String){
        val alertData = AlertData(
            title = "No network",
            message = "Added item to the cart queue"
        )
        _showAlert.value = alertData
    }

    fun addItemToCart(
        userId: Int,
        menuId: Int,
        quantity: Int,
        authorizationToken: String
    ){
        PlateDetailActivity.cartRepo?.let { cartRepo ->
            cartRepo.addItemToCart(
                userId = userId,
                menuId = menuId,
                quantity = quantity,
                authorizationToken = authorizationToken,
                callback = object : NetworkHandleCallback<ApiResponse<Cart>>{
                    override fun onConnected(data: ApiResponse<Cart>) {
                        _addCartResponseLiveData.value = data
                    }
                    override fun onDisconnected() {
                        showAlert("Added cart item to the queue")
                    }
                }
            )
        }
    }

}