package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.ActivityLoginBinding
import com.example.rallyapp.databinding.ActivityOrdersBinding
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.recyclerview_adpaters.OrdersAdapter
import com.example.rallyapp.recyclerview_adpaters.SingleOrderAdapter
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.OrderActivityViewModel
import java.util.TimerTask

class OrdersActivity : BaseActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var ordersAdapter: OrdersAdapter

    private lateinit var viewModel: OrderActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHeaderFragment()

        viewModel = ViewModelProvider(this)[OrderActivityViewModel::class.java]
        listenForAlertsFromViewModel()

        setOrdersListRecyclerView()

        ifUserLoggedIn {
            showLoadingScreen()
            viewModel.getUsersOrder(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
        }
        setObserverOnOrdersList()
        setListenerOnReorderResponse()

        //setting bottom nav listeners
        binding.newBottomNav.selectedItemId = R.id.orders_menu_item
        binding.newBottomNav.setOnNavigationItemSelectedListener {
            setMenu(it.itemId)
        }
    }

    private fun setHeaderFragment(){
        val fragment: Fragment = HeaderFragment.newInstance("check out your past orders!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()
    }


    private fun setOrdersListRecyclerView(){
        val gridViewItem = findViewById<RecyclerView>(R.id.orders_recyclerview)
        gridViewItem.layoutManager = GridLayoutManager(this, 1)
        ordersAdapter = OrdersAdapter(this, listOf(), viewModel, supportFragmentManager, ::showLoadingScreen)
        gridViewItem.adapter = ordersAdapter
    }

    private fun setObserverOnOrdersList(){
        viewModel.orderListLiveData.observe(this){
            hideLoadingScreen()
            ordersAdapter.setData(it)
        }
    }

    private fun listenForAlertsFromViewModel(){
        viewModel.showAlert.observe(this){ alertData ->
            hideLoadingScreen()
            val alertManager = AlertManager(this)
            alertManager.showAlertWithOkButton(alertData)
        }
    }

    private fun setListenerOnReorderResponse(){
        viewModel.reorderResponseLiveData.observe(this){
            hideLoadingScreen()
            if(it.success == 0){
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "Reorder Failed",
                    message = it.message
                ))
            }else{
                val intent = Intent(this, CheckoutActivity::class.java)
                val bundle = Bundle().apply {
                    putInt(CheckoutActivity.ORDER_ID_KEY, it.data[0].id)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }

        }
    }

    private fun ifUserLoggedIn(task: () -> Unit){
        if(UserCredentials.isUserSet()){
            task()
            return
        }

        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Please Log In",
            message = "Please log in before you can see your orders"
        )){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun showLoadingScreen(){
        binding.ordersActivityLoadingScreen.visibility = View.VISIBLE
    }

    fun hideLoadingScreen(){
        binding.ordersActivityLoadingScreen.visibility = View.GONE
    }

    fun goToOrders(v: View) {
    }
    fun goToSingleOrder(v: View) {
        val intent = Intent(this, SingleOrderActivity::class.java)
        startActivity(intent)
    }
}