package com.example.rallyapp.utils

import android.content.Intent
import android.util.Log
import com.example.rallyapp.activities.CheckoutActivity
import com.example.rallyapp.activities.OrdersActivity
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.viewModel.CheckoutActivityViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CheckoutActivity.addLiveDataObservers(viewModel: CheckoutActivityViewModel){

    // payment intent response
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
                PaymentConfiguration.init(this@addLiveDataObservers, paymentData.publishableKey)
                withContext(Dispatchers.Main) {
                    presentPaymentSheet()
                }
            }
        }
    }

    // directions
    viewModel.directionLiveData.observe(this){
        mapsManager.showDirection(it)
    }

    // add address response
    viewModel.addAddressResponse.observe(this) {
        handleResponseIfSuccess(
            response = it,
            message = ""
        ) {
            Log.i(CheckoutActivity.TAG, "success")
            viewModel.getUsersAddress(
                UserCredentials.getUserId()!!,
                UserCredentials.getToken()!!
            )
        }
    }

    // order confirmation
    viewModel.confirmOrderResponses.observe(this) {
        handleResponseIfSuccess(message = "", response = it) {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
            orderConfirmed = true
            finish()
        }
    }

    // address fetch response
    viewModel.userAddressResponse.observe(this) {
        handleResponseIfSuccess(
            response = it,
            message = "Failed to fetch users address",
            navigateBack = true,
        ) {
            addressListAdapter.setData(it.data)
        }
    }

    //order by id response
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
            setVoucher()
        }
    }
}
