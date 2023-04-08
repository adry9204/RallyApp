package com.example.rallyapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.repo.OrderRepo
import com.example.rallyapp.utils.AlertData

class SingleOrderActivityViewModel(application: Application): AndroidViewModel(application) {

    private val orderRepo = OrderRepo()

    private var _orderLiveData = MutableLiveData<Order<User>>()
    val orderLiveData: MutableLiveData<Order<User>> = _orderLiveData

    private var _reorderResponseLiveData = MutableLiveData<ApiResponse<Order<User>>>()
    val reorderResponseLiveData: MutableLiveData<ApiResponse<Order<User>>> = _reorderResponseLiveData



    private val _showAlert = MutableLiveData<AlertData>()
    val showAlert: LiveData<AlertData> = _showAlert

    fun showAlert(title: String, message: String){
        val alertData = AlertData(
            title = title,
            message = message
        )
        _showAlert.value = alertData
    }

    fun getOrderById(orderId: Int, token: String){
        orderRepo.getOrderById(orderId, token){
            if(it.success == 1){
                _orderLiveData.value = it.data[0]
            }else{
                showAlert(title = "Failed", message = "Failed to fetch order")
            }
        }
    }

    fun reorderByOrderId(orderId: Int, token: String){
        orderRepo.reorderById(orderId, token){
            _reorderResponseLiveData.value = it
        }
    }
}