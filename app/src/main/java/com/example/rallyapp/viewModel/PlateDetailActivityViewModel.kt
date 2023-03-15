package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.PlateDetailActivity
import com.example.rallyapp.dataModel.response_models.ApiResponse
import com.example.rallyapp.dataModel.response_models.Cart
import com.example.rallyapp.dataModel.response_models.Menu

class PlateDetailActivityViewModel: ViewModel() {

    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    val menuListLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    private var _addCartResponseLiveData = MutableLiveData<ApiResponse<Cart>>()
    val addCartResponseLiveData: MutableLiveData<ApiResponse<Cart>> = _addCartResponseLiveData


    fun getMenuItemById(menuId: Int){
        PlateDetailActivity.menuRepo?.let { menuRepo ->
            menuRepo.getMenuItemById(menuId){
                _menuListLiveData.value = it
            }
        }
    }

    fun addItemToCart(
        userId: Int,
        menuId: Int,
        quantity: Int,
        authorizationToken: String
    ){
        PlateDetailActivity.cartRepo?.let { cartRepo ->
            cartRepo.addCItemToCart(
                userId = userId,
                menuId = menuId,
                quantity = quantity,
                authorizationToken = authorizationToken
            ){
                _addCartResponseLiveData.value = it
            }
        }
    }



}