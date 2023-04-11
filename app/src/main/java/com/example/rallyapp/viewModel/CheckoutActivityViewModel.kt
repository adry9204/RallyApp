package com.example.rallyapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rallyapp.api.api_helpers.DirectionsApiHelper
import com.example.rallyapp.api.dataModel.maps_api.DirectionsApiResult
import com.example.rallyapp.api.dataModel.request_models.AddAddressRequest
import com.example.rallyapp.api.dataModel.response_models.*
import com.example.rallyapp.repo.AddressRepo
import com.example.rallyapp.repo.OrderRepo
import com.google.android.gms.maps.model.LatLng

class CheckoutActivityViewModel(application: Application): AndroidViewModel(application){

    companion object{
        const val TAG = "CheckoutActivityViewModel"
    }

    private val orderRepo = OrderRepo()
    private val addressRepo = AddressRepo()
    private val directionsApiHelper = DirectionsApiHelper()

    private var _getOrderByIdResponse = MutableLiveData<ApiResponse<Order<User>>>()
    val getOrderByIdResponse: MutableLiveData<ApiResponse<Order<User>>> = _getOrderByIdResponse

    private var _applyVoucherResponse = MutableLiveData<ApiResponse<Order<User>>>()
    val applyVoucherResponse: MutableLiveData<ApiResponse<Order<User>>> = _applyVoucherResponse

    private var _userAddressResponse = MutableLiveData<ApiResponse<Address<User>>>()
    val userAddressResponse: MutableLiveData<ApiResponse<Address<User>>> = _userAddressResponse

    private var _paymentIntentResponse = MutableLiveData<ApiResponse<StripePaymentDetails>>()
    val paymentIntentResponse: MutableLiveData<ApiResponse<StripePaymentDetails>> = _paymentIntentResponse

    private var _confirmOrderResponse = MutableLiveData<ApiResponse<Order<Int>>>()
    val confirmOrderResponses: MutableLiveData<ApiResponse<Order<Int>>> = _confirmOrderResponse

    private var _addAddressResponse = MutableLiveData<ApiResponse<Address<Int>>>()
    val addAddressResponse: MutableLiveData<ApiResponse<Address<Int>>> = _addAddressResponse

    private var _deleteAddressResponse = MutableLiveData<ApiResponse<Address<Int>>>()
    val deleteAddressResponse: MutableLiveData<ApiResponse<Address<Int>>> = _deleteAddressResponse

    private var _directionLiveData = MutableLiveData<DirectionsApiResult>()
    val directionLiveData: MutableLiveData<DirectionsApiResult> = _directionLiveData

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
        addressId: Int? = null,
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

    fun deleteAddress(
        addressId: Int,
        token: String
    ){
        addressRepo.deleteAddress(addressId, token){
            _deleteAddressResponse.value = it
        }
    }

    fun getDirections(origin: LatLng, destination: LatLng){
        val originString = makeLatLngToString(origin)
        val destinationString = makeLatLngToString(destination)
        directionsApiHelper.getDirections(originString, destinationString){
            it?.let {
               directionLiveData.value = it
            }
        }
    }

    fun applyVoucherToOrder(
        orderId: Int,
        voucherCode: String,
        token: String
    ){
        orderRepo.applyVoucherToOrder(orderId, voucherCode, token){
            _applyVoucherResponse.value = it
        }
    }

    fun makeLatLngToString(latLng: LatLng): String{
        return ("${latLng.latitude},${latLng.longitude}")
    }
}