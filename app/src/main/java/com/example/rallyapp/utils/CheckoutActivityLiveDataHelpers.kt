package com.example.rallyapp.utils

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.example.rallyapp.R
import com.example.rallyapp.activities.CheckoutActivity
import com.example.rallyapp.activities.OrdersActivity
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.viewModel.CheckoutActivityViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import kotlinx.coroutines.*

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
                    hideLoading()
                    presentPaymentSheet()
                }
            }
        }
    }

    // directions
    viewModel.directionLiveData.observe(this){
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            mapsManager.showDirection(it)
        }
        binding.checkoutActivityDirectionDuration.text = it.routes[0].legs[0].duration.text
        binding.checkoutActivityDirectionDistance.text =  it.routes[0].legs[0].distance.text
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
            hideLoading()
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
            orderConfirmed = true
            finish()
        }
    }

    // address fetch response
    viewModel.userAddressResponse.observe(this) {
        hideLoading()
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
        hideLoading()
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

    //applyVoucher
    viewModel.applyVoucherResponse.observe(this){
        Log.i("Test", it.toString())
        hideLoading()
        if(it.success == 1){
            orderItemsAdapter.setData(it.data[0].orderDetails)
            order = it.data[0]
            binding.orderActivityOrderSummaryTotalPriceValue.text =
                "$${it.data[0].beforeTaxPrice}"
            binding.orderActivityOrderSummaryTaxPriceValue.text = "$${it.data[0].taxPrice}"
            binding.orderActivityOrderSummaryGrandTotalValue.text = "$${it.data[0].totalPrice}"
            binding.orderActivityVoucherResponse.setTextColor(
                ContextCompat.getColor(this, R.color.emerald_green)
            )
            setVoucher()
        }else{
            binding.orderActivityVoucherResponse.text = it.message
            binding.orderActivityVoucherResponse.visibility = View.VISIBLE
            binding.orderActivityVoucherResponse.setTextColor(
                ContextCompat.getColor(this, R.color.warning_red)
            )
            binding.checkoutActivityVoucherLineBottom.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = binding.orderActivityVoucherResponse.id
            }
        }
    }

    viewModel.deleteAddressResponse.observe(this){
        hideLoading()
        viewModel.getUsersAddress(UserCredentials.getUserId()!!, UserCredentials.getToken() !!)
    }
}
