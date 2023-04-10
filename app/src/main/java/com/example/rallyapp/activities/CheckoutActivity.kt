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


    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var orderItemsAdapter: OrderDetailsListAdapter
    private lateinit var addressListAdapter: AddressListAdapter

    private lateinit var viewModel: CheckoutActivityViewModel

    private lateinit var order: Order<User>

    //stripe variables
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private lateinit var paymentIntentClientSecret: String

    private lateinit var orderMethodSelector: OrderMethodSelector

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mMap: GoogleMap
    private lateinit var mapsManager: CheckoutActivityMapsManager

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    var orderConfirmed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentSheet = PaymentSheet(this, this::onPaymentSheetResult)

        viewModel = ViewModelProvider(this)[CheckoutActivityViewModel::class.java]

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    getDirectionFromMyLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    getDirectionFromMyLocation()
                }
                else -> {

                }
            }
        }

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

        binding.checkoutActivityAddAddressButton.setOnClickListener {
            showAddAddressBottomSheet()
        }

        setOrderMethodSelector()
        setObserverOnConfirmOrderResponse()
        setObserverOnAddAddress()
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
                    handleLocationPermission {  }
                }
                binding.checkoutActivityMethodAddressSection.visibility = View.GONE
                binding.checkoutActivityMethodMapSection.visibility = View.VISIBLE
            }
        }

        setObserverOnDirectionsLiveData()
    }

    private fun setObserverOnDirectionsLiveData(){
        viewModel.directionLiveData.observe(this){
            showDirection(it)
        }
    }

    private fun showDirection(direction: DirectionsApiResult){
        Log.i(TAG, direction.toString())
        var path = mutableListOf<List<LatLng>>()
        for (steps in direction.routes[0].legs[0].steps){
            val point = steps.polyline.points
            path.add(PolyLineUtil.decodePolyLines(point)!!)
        }

        for (points in path){
            mMap.addPolyline(PolylineOptions().addAll(points).color(Color.RED))
        }
    }

    private fun setMap() {
        if (!::mMap.isInitialized) {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.checkout_activity_maps_fragment) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                mMap = googleMap
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(43.7173112591, -79.3055932409))
                        .title("Rally Restaurant & Map")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(43.7174, -79.3054),
                        12.0f
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::order.isInitialized && !orderConfirmed) {
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
                    withContext(Dispatchers.Main) {
                        presentPaymentSheet()
                    }
                }
            }
        }
    }

    private fun setObserverOnAddAddress() {
        viewModel.addAddressResponse.observe(this) {
            handleResponseIfSuccess(
                response = it,
                message = ""
            ) {
                Log.i(TAG, "success")
                viewModel.getUsersAddress(
                    UserCredentials.getUserId()!!,
                    UserCredentials.getToken()!!
                )
            }
        }
    }

    private fun setObserverOnConfirmOrderResponse() {
        viewModel.confirmOrderResponses.observe(this) {
            handleResponseIfSuccess(message = "", response = it) {
                val intent = Intent(this, OrdersActivity::class.java)
                startActivity(intent)
                orderConfirmed = true
                finish()
            }
        }
    }

    private fun presentPaymentSheet() {
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
                setVoucher()
            }
        }
    }

    private fun setVoucher() {
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
            binding.orderActivityOrderSummaryDiscountLabel.text = "${it.offerPercent}% off saved $${
                Float.valueOf(order.beforeTaxPrice) - Float.valueOf(order.afterOfferPrice)
            }"
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

    private fun handleResponseIfSuccess(
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

    @SuppressLint("MissingPermission")
    private fun getDirectionFromMyLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val usersLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions()
                    .position(usersLocation)
                    .title("Rally Restaurant & Map")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                )
                viewModel.getDirections(usersLocation, LatLng(43.7173112591, -79.3055932409))
            }
        }
    }

    private fun handleLocationPermission(task: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getDirectionFromMyLocation()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION) -> {
                requestPermission()
            }
            else -> { requestPermission() }
        }
    }

    private fun requestPermission() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }
}