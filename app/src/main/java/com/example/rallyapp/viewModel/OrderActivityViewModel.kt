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

class OrderActivityViewModel(application: Application): AndroidViewModel(application) {

    private val orderRepo = OrderRepo()

    private var _orderListLiveData = MutableLiveData<List<Order<User>>>()
    val orderListLiveData: MutableLiveData<List<Order<User>>> = _orderListLiveData

    private var _reorderResponseLiveData = MutableLiveData<ApiResponse<Order<User>>>()
    val reorderResponseLiveData: MutableLiveData<ApiResponse<Order<User>>> = _reorderResponseLiveData

    private val _showAlert = MutableLiveData<AlertData>()
    val showAlert: LiveData<AlertData> = _showAlert

    fun getUsersOrder(userId: Int, token: String){
        orderRepo.getUsersOrder(userId, token){
            if(it.success == 1){
                _orderListLiveData.value = it.data
            }else{
                showAlert(title = "Error", message = it.message)
            }
        }
    }

    fun reorderByOrderId(orderId: Int, token: String){
        orderRepo.reorderById(orderId, token){
            _reorderResponseLiveData.value = it
        }
    }

    private fun showAlert(title: String, message: String){
        val alertData = AlertData(
            title = title,
            message = message
        )
        _showAlert.value = alertData
    }
}
