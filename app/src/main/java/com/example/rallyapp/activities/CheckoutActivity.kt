package com.example.rallyapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rallyapp.api.dataModel.response_models.*
import com.example.rallyapp.databinding.ActivityCheckoutBinding
import com.example.rallyapp.fragments.AddAddressBottomSheetFragment
import com.example.rallyapp.recyclerview_adpaters.AddressListAdapter
import com.example.rallyapp.recyclerview_adpaters.OrderDetailsListAdapter
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.CheckoutActivityViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.*



class CheckoutActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CheckoutActivity"
        const val ORDER_ID_KEY = "order_data"
    }


    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var orderItemsAdapter: OrderDetailsListAdapter
    private lateinit var addressListAdapter: AddressListAdapter

    private lateinit var viewModel: CheckoutActivityViewModel

    private lateinit var order: Order<User>

    //stripe variables
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    var orderConfirmed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentSheet = PaymentSheet(this, this::onPaymentSheetResult)

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

        setObserverOnPaymentIntentResponse()

        binding.checkoutActivityProceedToPayButton.setOnClickListener {
            if (addressListAdapter.selectedAddressId != null) {
                doIfUserLoggedIn {
                    viewModel.makePaymentIntent(
                        orderId = order.id,
                        addressId = addressListAdapter.selectedAddressId!!,
                        token = UserCredentials.getToken()!!
                    )
                }
            } else {
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(
                    AlertData(
                        title = "Missing Address",
                        message = "Please select a delivery location"
                    )
                )
            }
        }

        binding.checkoutActivityCancelCheckoutButton.setOnClickListener {
            finish()
        }

        binding.checkoutActivityAddAddressButton.setOnClickListener{
            showAddAddressBottomSheet()
        }

        setObserverOnConfirmOrderResponse()
        setObserverOnAddAddress()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(this::order.isInitialized && !orderConfirmed){
            viewModel.deleteOrder(orderId = order.id, UserCredentials.getToken()!!)
        }
    }

    private fun showAddAddressBottomSheet(){
        val addAddressBottomSheetFragment = AddAddressBottomSheetFragment(viewModel)
        addAddressBottomSheetFragment.show(supportFragmentManager, addAddressBottomSheetFragment.tag)
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult){
        val alertManager = AlertManager(this)
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.i(TAG, "Canceled payment")
            }
            is PaymentSheetResult.Failed -> {
                alertManager.showAlertWithOkButton(AlertData(
                   title = "Error",
                   message = paymentSheetResult.error.message.toString()
               ))
            }
            is PaymentSheetResult.Completed -> {
                viewModel.orderConfirmed(orderId = order.id, UserCredentials.getToken()!!)
            }
        }
    }

    private fun setObserverOnPaymentIntentResponse() {
        viewModel.paymentIntentResponse.observe(this) {
            handleResponseIfSuccess(
                response = it,
                message = "Failed to make payment",
            ) {

                CoroutineScope(Dispatchers.IO).launch {
                    val paymentData = it.data[0]
                    paymentIntentClientSecret = paymentData.paymentIntent
                    customerConfig = PaymentSheet.CustomerConfiguration(
                        paymentData.customer,
                        paymentData.ephemeralKey
                    )
                    PaymentConfiguration.init(this@CheckoutActivity, paymentData.publishableKey)
                    withContext(Dispatchers.Main){
                        presentPaymentSheet()
                    }
                }
            }
        }
    }

    private fun setObserverOnAddAddress(){
        viewModel.addAddressResponse.observe(this){
            handleResponseIfSuccess(
                response = it,
                message = ""
            ){
                Log.i(TAG, "success")
                viewModel.getUsersAddress(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
            }
        }
    }

    private fun setObserverOnConfirmOrderResponse(){
        viewModel.confirmOrderResponses.observe(this){
            handleResponseIfSuccess(message = "", response = it){
                val intent = Intent(this, OrdersActivity::class.java)
                startActivity(intent)
                orderConfirmed = true
                finish()
            }
        }
    }

    private fun presentPaymentSheet(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Rally Restaurant & Bar.",
                customer = customerConfig,
            )
        )
    }

    private fun setAddressListRecyclerView() {
        binding.orderActivityAddressRecyclerView.layoutManager = LinearLayoutManager(this)
        addressListAdapter = AddressListAdapter(this, mutableListOf())
        binding.orderActivityAddressRecyclerView.adapter = addressListAdapter
    }

    private fun setObserverOnUsersAddressResponse() {
        viewModel.userAddressResponse.observe(this) {
            handleResponseIfSuccess(
                response = it,
                message = "Failed to fetch users address",
                navigateBack = true,
            ) {
                addressListAdapter.setData(it.data)
            }
        }
    }

    private fun setOrderItemsRecyclerView() {
        binding.orderActivityOrderItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        orderItemsAdapter = OrderDetailsListAdapter(listOf())
        binding.orderActivityOrderItemsRecyclerView.adapter = orderItemsAdapter
    }

    private fun setObserverOnGetOrderByItemResponse() {
        viewModel.getOrderByIdResponse.observe(this) {
            handleResponseIfSuccess(
                response = it,
                message = "Failed to fetch order",
                navigateBack = true
            ) {
                orderItemsAdapter.setData(it.data[0].orderDetails)
                order = it.data[0]
                binding.orderActivityOrderSummaryTotalPriceValue.text =
                    "$${it.data[0].beforeTaxPrice}"
                binding.orderActivityOrderSummaryTaxPriceValue.text = "$${it.data[0].taxPrice}"
                binding.orderActivityOrderSummaryGrandTotalValue.text = "$${it.data[0].totalPrice}"
            }
        }
    }

    private fun doIfUserLoggedIn(task: () -> Unit) {
        if (UserCredentials.isUserSet()) {
            task()
        } else {
            val alertManager = AlertManager(this)
            alertManager.showAlertWithOkButton(
                AlertData(
                    title = "Please Login",
                    message = "Please login before checking out"
                )
            ) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun handleResponseIfSuccess(
        message: String,
        navigateBack: Boolean = false,
        response: ApiResponse<*>, task: () -> Unit
    ) {
        if (response.success == 1) {
            task()
        } else {
            val alertManager = AlertManager(this)
            alertManager.showAlertWithOkButton(
                AlertData(
                    title = "Failed",
                    message = response.message
                )
            ) {
                if (navigateBack) {
                    finish()
                }
            }
        }
    }
}