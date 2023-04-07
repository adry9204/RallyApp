package com.example.rallyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.OrderDetail
import com.example.rallyapp.databinding.ActivityCheckoutBinding
import com.example.rallyapp.recyclerview_adpaters.AddressListAdapter
import com.example.rallyapp.recyclerview_adpaters.OrderDetailsListAdapter
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.CheckoutActivityViewModel


class CheckoutActivity : AppCompatActivity() {

    companion object{
        const val TAG = "CheckoutActivity"
        const val ORDER_ID_KEY = "order_data"
    }


    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var orderItemsAdapter: OrderDetailsListAdapter
    private lateinit var addressListAdapter: AddressListAdapter

    private lateinit var viewModel: CheckoutActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CheckoutActivityViewModel::class.java]

        setOrderItemsRecyclerView()
        setObserverOnGetOrderByItemResponse()

        intent.extras?.let {
            it.getInt(ORDER_ID_KEY)?.let {
                doIfUserLoggedIn {
                    viewModel.getOrderById(orderId = it, UserCredentials.getToken()!!)
                }
            }
        }

        setAddressListRecyclerView()
        setObserverOnUsersAddressResponse()

       doIfUserLoggedIn {
           viewModel.getUsersAddress(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
       }
    }

    private fun setAddressListRecyclerView(){
        binding.orderActivityAddressRecyclerView.layoutManager = LinearLayoutManager(this)
        addressListAdapter = AddressListAdapter(this, mutableListOf())
        binding.orderActivityAddressRecyclerView.adapter = addressListAdapter
    }

    private fun setObserverOnUsersAddressResponse(){
        viewModel.userAddressResponse.observe(this){
            if(it.success == 1){
                addressListAdapter.setData(it.data)
            }else{
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "Failed to make order",
                    message = "Failed to fetch users address"
                )){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setOrderItemsRecyclerView(){
        binding.orderActivityOrderItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        orderItemsAdapter = OrderDetailsListAdapter(listOf())
        binding.orderActivityOrderItemsRecyclerView.adapter = orderItemsAdapter
    }

    private fun setObserverOnGetOrderByItemResponse(){
        viewModel.getOrderByIdResponse.observe(this){
            if(it.success == 1){
                orderItemsAdapter.setData(it.data[0].orderDetails)
                binding.orderActivityOrderSummaryTotalPriceValue.text = "$${it.data[0].beforeTaxPrice}"
                binding.orderActivityOrderSummaryTaxPriceValue.text = "$${it.data[0].taxPrice}"
                binding.orderActivityOrderSummaryGrandTotalValue.text = "$${it.data[0].totalPrice}"
            }else{
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "Failed to make order",
                    message = "Failed to fetch order"
                )){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun doIfUserLoggedIn(task: ()->Unit){
        if(UserCredentials.isUserSet()){
            task()
        }else{
            val alertManager = AlertManager(this)
            alertManager.showAlertWithOkButton(AlertData(
                title = "Please Login",
                message = "Please login before checking out"
            )){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}