package com.example.rallyapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.databinding.ActivitySingleOrderBinding
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.recyclerview_adpaters.CartAdapter
import com.example.rallyapp.recyclerview_adpaters.GridViewItemAdapter
import com.example.rallyapp.recyclerview_adpaters.SingleOrderAdapter
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.HomeActivityViewModel
import com.example.rallyapp.viewModel.OrderActivityViewModel
import com.example.rallyapp.viewModel.SingleOrderActivityViewModel
import java.text.SimpleDateFormat
import java.util.*

class SingleOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleOrderBinding
    private lateinit var adapter: SingleOrderAdapter
    private lateinit var viewModel: SingleOrderActivityViewModel

    private lateinit var order: Order<User>

    companion object {
        const val ORDER_ID_KEY = "order_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SingleOrderActivityViewModel::class.java]

        addHeaderFragment()
        setSingleOrderRecyclerView()

        ifUserLoggedIn {
            intent.extras?.let {
                it.getInt(ORDER_ID_KEY).let { orderId ->
                    viewModel.getOrderById(orderId, UserCredentials.getToken()!!)
                }
            }
        }
        setObserverOnOrderData()

        binding.reorderDetailButton.setOnClickListener {
            ifUserLoggedIn {
                viewModel.reorderByOrderId(order.id, UserCredentials.getToken()!!)
            }
        }

        //setting bottom nav listeners
        binding.newBottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_menu_item -> {
                    var intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.search_menu_item -> {
                    var intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.carts_menu_item -> {
                    true
                }
                R.id.orders_menu_item ->{
                    var intent = Intent(this, OrdersActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.user_menu_item -> {
                    var intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        setObserverOnReorderResponse()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun setObserverOnReorderResponse(){
        viewModel.reorderResponseLiveData.observe(this){
            if(it.success == 1){
                val bundle = Bundle().apply {
                    putInt(CheckoutActivity.ORDER_ID_KEY, it.data[0].id)
                }
                val intent = Intent(this, CheckoutActivity::class.java).apply {
                    putExtras(bundle)
                }
                startActivity(intent)
            }else{
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "Failed to Reorder",
                    message = "Failed to reorder the order"
                ))
            }
        }
    }

    private fun setObserverOnOrderData(){
        viewModel.orderLiveData.observe(this){
            adapter.setData(it.orderDetails)
            binding.dateSingleOrderLabel.text = makeDatePriceString(it.orderPlacedDate!!, it.totalPrice).toEditable()
            order = it
        }
    }

    private fun setSingleOrderRecyclerView(){
        binding.singleOrderRecyclerview.layoutManager = GridLayoutManager(this, 1)
        adapter = SingleOrderAdapter(listOf())
        binding.singleOrderRecyclerview.adapter = adapter
    }

    private fun addHeaderFragment(){
        val fragment: Fragment = HeaderFragment.newInstance("check out the details of this order!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()
    }

    private fun ifUserLoggedIn(task: () -> Unit){
        if(UserCredentials.isUserSet()){
            task()
            return
        }

        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(
            AlertData(
            title = "Please Log In",
            message = "Please log in before you can see your orders"
        )
        ){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun makeDatePriceString(date: Date, price: String): String{
        val dateString = formatDate(date)
        return "$dateString - $$price"
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return format.format(date)
    }

}