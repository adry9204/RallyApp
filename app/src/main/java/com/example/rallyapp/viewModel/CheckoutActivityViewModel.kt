package com.example.rallyapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.repo.AddressRepo
import com.example.rallyapp.repo.OrderRepo

class CheckoutActivityViewModel(application: Application): AndroidViewModel(application){

    private val orderRepo = OrderRepo()
    private val addressRepo = AddressRepo()

    private var _getOrderByIdResponse = MutableLiveData<ApiResponse<Order<User>>>()
    val getOrderByIdResponse: MutableLiveData<ApiResponse<Order<User>>> = _getOrderByIdResponse

    private var _userAddressResponse = MutableLiveData<ApiResponse<Address<User>>>()
    val userAddressResponse: MutableLiveData<ApiResponse<Address<User>>> = _userAddressResponse

    fun getOrderById(orderId: Int, token: String){
        orderRepo.getOrderById(orderId, token){
            _getOrderByIdResponse.value = it
        }
    }

    fun getUsersAddress(userId: Int, token: String){
        addressRepo.getUsersAddress(userId, token){
            userAddressResponse.value = it
        }
    }
}