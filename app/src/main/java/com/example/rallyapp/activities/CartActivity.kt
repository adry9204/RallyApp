package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rallyapp.recyclerview_adpaters.CartAdapter
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.databinding.ActivityCartBinding
import com.example.rallyapp.repo.CartRepo
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.CartActivityViewModel
import com.google.android.material.snackbar.Snackbar

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private var adapter: CartAdapter? = null
    private lateinit var viewModel: CartActivityViewModel

    companion object {
        const val TAG = "CartActivity"
        var cartRepo: CartRepo? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartRepo = CartRepo(this)

        if(!UserCredentials.isUserSet()){ gotToLoginActivity() }
        viewModel = ViewModelProvider(this)[CartActivityViewModel::class.java]

        showLoading()
        viewModel.getUserCart(UserCredentials.getUserId() ?: 0, UserCredentials.getToken() ?: "")

        //setting the header with the correct tagline
        var fragment: Fragment = HeaderFragment.newInstance("check your products in cart!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        setObserverOnCartData()
        setObserverOnCartDeleteResponse()
        setObserverOnCartQuantityResponse()
        setObserverOnAlertMessages()
        setObserverOnMakeOrderFromCartResponse()

        binding.checkoutButton.setOnClickListener {
            if(UserCredentials.isUserSet()){
                if(UserCredentials.hasVoucher()){
                    showLoading()
                    viewModel.makeOrderFromCart(
                        userId = UserCredentials.getUserId()!!,
                        voucherId = UserCredentials.getVoucher()!!.id,
                        token = UserCredentials.getToken()!!)
                }else {
                    showLoading()
                    viewModel.makeOrderFromCart(
                        userId = UserCredentials.getUserId()!!,
                        token = UserCredentials.getToken()!!)
                }
            }else{
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "Please login",
                    message = "You need to login before checking out"
                )){
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.shoppingCartRecyclerview.layoutManager = GridLayoutManager(this, 1)
        adapter = CartAdapter(mutableListOf(), viewModel, this, ::showLoading)
        binding.shoppingCartRecyclerview.adapter = adapter
        binding.shoppingCartRecyclerview.itemAnimator = DefaultItemAnimator()

    }

    private fun setObserverOnMakeOrderFromCartResponse(){
        viewModel.makeOrderFromCartResponse.observe(this){
            if(it.success == 1){
                Log.i(TAG, "Response -> $it")
                val intent = Intent(this, CheckoutActivity::class.java)
                val bundle = Bundle().apply {
                    putInt(CheckoutActivity.ORDER_ID_KEY, it.data[0].id)
                }
                intent.putExtras(bundle)
                startActivity(intent)
                hideLoading()
            }else{
                hideLoading()
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(
                    AlertData(
                    title = "Failed to make order",
                    message = it.message
                ))
            }
        }
    }

    private fun setObserverOnCartData(){
        viewModel.cartLiveData.observe(this){
            hideLoading()
            adapter?.let { adapter->
                adapter.setData(it.toMutableList())
                adapter.notifyDataSetChanged()
            }
            val totalCartPrice = calculateTotalFromCart(it)
            binding.totalAmountLabelCart.text = "$$totalCartPrice"
        }
    }

    private fun setObserverOnCartDeleteResponse() {
        viewModel.cartResponseLiveData.observe(this){
            hideLoading()
            if(it.success == 1){
                makeSnackBar("deleted cart item successfully", binding.shoppingCartRecyclerview)
                adapter?.let { adapter ->
                    adapter.onDeleteSuccess()
                }
            }else{
                adapter?.let { adapter ->
                    adapter.onDeleteFailed()
                }
                makeSnackBar("failed to delete cart item ", binding.shoppingCartRecyclerview)
            }
        }
    }

    private fun setObserverOnCartQuantityResponse(){
        viewModel.updateQuantityResponse.observe(this){
            if(it.success != 1){
                viewModel.getUserCart(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
            }
        }
    }

    private fun calculateTotalFromCart(cartItems: List<Cart>): Float{
        var totalPrice = 0.0f
        for (cartItem in cartItems){
            totalPrice += cartItem.price.toFloat()
        }
        return totalPrice
    }

    private fun setObserverOnAlertMessages(){
        viewModel.showAlert.observe(this){
            hideLoading()
            val alertManager = AlertManager(this)
            alertManager.showAlertWithOkButton(it)
        }
    }

    private fun makeSnackBar(message: String, view: View){
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun gotToLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun goToOrders(v:View) {
        var intent = Intent(this, OrdersActivity::class.java)
        startActivity(intent)
    }

    fun showLoading(){
        binding.cartActivityActivityLoadingScreen.visibility = View.VISIBLE
    }

    fun hideLoading(){
        binding.cartActivityActivityLoadingScreen.visibility = View.GONE
    }

}