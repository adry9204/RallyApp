package com.example.rallyapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rallyapp.api.dataModel.request_models.AddAddressRequest
import com.example.rallyapp.api.dataModel.response_models.*
import com.example.rallyapp.repo.AddressRepo
import com.example.rallyapp.repo.OrderRepo

class CheckoutActivityViewModel(application: Application): AndroidViewModel(application){

    companion object{
        const val TAG = "CheckoutActivityViewModel"
    }

    private val orderRepo = OrderRepo()
    private val addressRepo = AddressRepo()

    private var _getOrderByIdResponse = MutableLiveData<ApiResponse<Order<User>>>()
    val getOrderByIdResponse: MutableLiveData<ApiResponse<Order<User>>> = _getOrderByIdResponse

    private var _userAddressResponse = MutableLiveData<ApiResponse<Address<User>>>()
    val userAddressResponse: MutableLiveData<ApiResponse<Address<User>>> = _userAddressResponse

    private var _paymentIntentResponse = MutableLiveData<ApiResponse<StripePaymentDetails>>()
    val paymentIntentResponse: MutableLiveData<ApiResponse<StripePaymentDetails>> = _paymentIntentResponse

    private var _confirmOrderResponse = MutableLiveData<ApiResponse<Order<Int>>>()
    val confirmOrderResponses: MutableLiveData<ApiResponse<Order<Int>>> = _confirmOrderResponse

    private var _addAddressResponse = MutableLiveData<ApiResponse<Address<Int>>>()
    val addAddressResponse: MutableLiveData<ApiResponse<Address<Int>>> = _addAddressResponse


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

    fun makePaymentIntent(
        orderId: Int,
        addressId: Int,
        token: String
    ){
        orderRepo.makePaymentIntent(orderId, addressId, token){
            _paymentIntentResponse.value = it
        }
    }

    fun orderConfirmed(
        orderId: Int,
        token: String
    ){
        orderRepo.confirmOrder(orderId, token){
            _confirmOrderResponse.value = it
        }
    }

    fun deleteOrder(
        orderId: Int,
        token: String
    ){
        orderRepo.deleteOrder(orderId, token){
            Log.i(TAG, "DeleteOrder Response -> $it")
        }
    }

    fun addNewAddress(
        addAddressRequest: AddAddressRequest,
        token: String
    ){
        addressRepo.addNewAddress(addAddressRequest, token){
            _addAddressResponse.value = it
        }
    }
}