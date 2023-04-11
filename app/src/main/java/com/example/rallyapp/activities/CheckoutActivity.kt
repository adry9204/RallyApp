package com.example.rallyapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rallyapp.R
import com.example.rallyapp.api.api_helpers.DirectionsApiHelper
import com.example.rallyapp.api.dataModel.maps_api.DirectionsApiResult
import com.example.rallyapp.api.dataModel.response_models.*
import com.example.rallyapp.databinding.ActivityCheckoutBinding
import com.example.rallyapp.fragments.AddAddressBottomSheetFragment
import com.example.rallyapp.recyclerview_adpaters.AddressListAdapter
import com.example.rallyapp.recyclerview_adpaters.OrderDetailsListAdapter
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.*
import com.example.rallyapp.viewModel.CheckoutActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.*
import java.lang.Float


class CheckoutActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CheckoutActivity"
        const val ORDER_ID_KEY = "order_data"
    }


    lateinit var binding: ActivityCheckoutBinding
    lateinit var orderItemsAdapter: OrderDetailsListAdapter
    lateinit var addressListAdapter: AddressListAdapter

    private lateinit var viewModel: CheckoutActivityViewModel

    lateinit var order: Order<User>

    //stripe variables
    private lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    private lateinit var orderMethodSelector: OrderMethodSelector

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mMap: GoogleMap
    lateinit var mapsManager: CheckoutActivityMapsManager

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    var orderConfirmed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentSheet = PaymentSheet(this, this::onPaymentSheetResult)

        viewModel = ViewModelProvider(this)[CheckoutActivityViewModel::class.java]

        locationPermissionRequest = registerRequestPermission {
            getDirectionFromMyLocation()
        }

        setOrderItemsRecyclerView()

        intent.extras?.let {
            it.getInt(ORDER_ID_KEY)?.let {
                doIfUserLoggedIn {
                    showLoading()
                    viewModel.getOrderById(orderId = it, UserCredentials.getToken()!!)
                }
            }
        }

        setAddressListRecyclerView()

        doIfUserLoggedIn {
            showLoading()
            viewModel.getUsersAddress(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
        }

        addLiveDataObservers(viewModel)

        binding.checkoutActivityProceedToPayButton.setOnClickListener {
            proceedToPay()
        }

        binding.checkoutActivityCancelCheckoutButton.setOnClickListener {
            finish()
        }

        binding.checkoutActivityAddAddressButton.setOnClickListener {
            showAddAddressBottomSheet()
        }

        binding.orderActivityVoucherApplyButton.setOnClickListener{
            showLoading()
            val voucherCode = binding.orderActivityVoucherTextBox.text.toString()
            viewModel.applyVoucherToOrder(order.id, voucherCode, UserCredentials.getToken()!!)
        }

        setOrderMethodSelector()
    }

    private fun proceedToPay(){
        if(orderMethodSelector.getSelected() == OrderMethodSelector.PICK_UP){
            doIfUserLoggedIn {
                showLoading()
                viewModel.makePaymentIntent(
                    orderId = order.id,
                    token = UserCredentials.getToken()!!
                )
            }
        }else{
            if (addressListAdapter.selectedAddressId != null) {
                doIfUserLoggedIn {
                    showLoading()
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




    }

    private fun setOrderMethodSelector() {
        orderMethodSelector = OrderMethodSelectorBuilder(
            this,
            binding.checkoutActivityMethodSelection,
            binding.checkoutActivityMethodSelectionDelivery,
            binding.checkoutActivityMethodSelectionPickup
        ).build()

        orderMethodSelector.setOnTapListener {
            if (it == OrderMethodSelector.DELIVERY) {
                binding.checkoutActivityMethodAddressSection.visibility = View.VISIBLE
                binding.checkoutActivityMethodMapSection.visibility = View.GONE
            } else {
                if(!::mMap.isInitialized){
                    setMap()
                    val permissionController = PermissionController(this)
                    permissionController.handleLocationPermission(locationPermissionRequest){
                        getDirectionFromMyLocation()
                    }
                }
                binding.checkoutActivityMethodAddressSection.visibility = View.GONE
                binding.checkoutActivityMethodMapSection.visibility = View.VISIBLE
            }
        }
    }


    private fun setMap() {
        if (!::mMap.isInitialized) {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.checkout_activity_maps_fragment) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                mMap = googleMap
                mapsManager = CheckoutActivityMapsManager(mMap)
                mapsManager.addMarker(LocationController.RALLY_LOCATION, "Rally Restaurant & Bar", CheckoutActivityMapsManager.MARKER_TYPE_RESTAURANT)
                mapsManager.animateCameraToLocation(LocationController.RALLY_LOCATION, 11.0f)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::order.isInitialized && !orderConfirmed) {
            showLoading()
            viewModel.deleteOrder(orderId = order.id, UserCredentials.getToken()!!)
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun showAddAddressBottomSheet() {
        val addAddressBottomSheetFragment = AddAddressBottomSheetFragment(viewModel)
        addAddressBottomSheetFragment.show(
            supportFragmentManager,
            addAddressBottomSheetFragment.tag
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        val alertManager = AlertManager(this)
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.i(TAG, "Canceled payment")
            }
            is PaymentSheetResult.Failed -> {
                alertManager.showAlertWithOkButton(
                    AlertData(
                        title = "Error",
                        message = paymentSheetResult.error.message.toString()
                    )
                )
            }
            is PaymentSheetResult.Completed -> {
                showLoading()
                viewModel.orderConfirmed(orderId = order.id, UserCredentials.getToken()!!)
            }
        }
    }


    fun presentPaymentSheet() {
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
        addressListAdapter = AddressListAdapter(this, viewModel, mutableListOf())
        binding.orderActivityAddressRecyclerView.adapter = addressListAdapter
    }

    private fun setOrderItemsRecyclerView() {
        binding.orderActivityOrderItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        orderItemsAdapter = OrderDetailsListAdapter(listOf())
        binding.orderActivityOrderItemsRecyclerView.adapter = orderItemsAdapter
    }

    fun setVoucher() {
        order.voucher?.let {
            binding.orderActivityVoucherTextBox.text = it.code.toEditable()
            binding.orderActivityVoucherResponse.text = "voucher applied ${it.offerPercent}% off"
            binding.orderActivityVoucherResponse.visibility = View.VISIBLE
            binding.checkoutActivityVoucherLineBottom.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = binding.orderActivityVoucherResponse.id
            }
            binding.checkoutActivityOrderSummaryDiscount.visibility = View.VISIBLE

            addStrikeTexOnTextView(
                binding.orderActivityOrderSummaryTotalPriceValue,
                "$${order.beforeTaxPrice} $${order.afterOfferPrice}",
                "$${order.beforeTaxPrice}"
            )
            val savings = Float.valueOf(order.beforeTaxPrice) - Float.valueOf(order.afterOfferPrice!!)
            val discountLabel =  "${it.offerPercent}% off saved $${savings}"
            binding.orderActivityOrderSummaryDiscountLabel.text = discountLabel
        }
    }

    private fun addStrikeTexOnTextView(tv: TextView, fullText: String, strokedSubText: String) {
        val spannable = SpannableString(fullText)
        val strikeThroughSpan = StrikethroughSpan()

        val startIndex = fullText.indexOf(strokedSubText)
        val endIndex = startIndex + strokedSubText.length

        spannable.setSpan(strikeThroughSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = spannable
    }

    private fun doIfUserLoggedIn(task: () -> Unit) {
        if (UserCredentials.isUserSet()) {
            task()
            return
        }
        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Please Login",
            message = "Please login before checking out"
        )) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun handleResponseIfSuccess(
        message: String,
        navigateBack: Boolean = false,
        response: ApiResponse<*>, task: () -> Unit
    ) {
        if (response.success == 1) {
            task()
            return
        }
        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Failed",
            message = response.message
        )) {
            if (navigateBack) {
                finish()
            }
        }
    }

    private fun getDirectionFromMyLocation() {
        val locationController = LocationController(this)
        locationController.getCurrentLocation {
            mapsManager.addMarker(it, "Your location", CheckoutActivityMapsManager.MARKER_TYPE_MY_LOCATION)
            viewModel.getDirections(it, LocationController.RALLY_LOCATION)
        }
    }

    fun showLoading(){
        binding.checkoutActivityLoadingScreen.visibility = View.VISIBLE
    }

    fun hideLoading(){
        binding.checkoutActivityLoadingScreen.visibility = View.GONE
    }

}