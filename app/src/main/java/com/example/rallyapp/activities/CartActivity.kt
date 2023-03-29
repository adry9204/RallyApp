package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rallyapp.recyclerview_adpaters.CartAdapter
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.R
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

        binding.shoppingCartRecyclerview.layoutManager = GridLayoutManager(this, 1)
        adapter = CartAdapter(mutableListOf<Cart>(), viewModel, this)
        binding.shoppingCartRecyclerview.adapter = adapter
        binding.shoppingCartRecyclerview.itemAnimator = DefaultItemAnimator()

    }

    private fun setObserverOnCartData(){
        viewModel.cartLiveData.observe(this){
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


    //Auxiliary Fragments Methods
    //Header
    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(v: View){
        var intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
    fun goToCart(v: View){
    }
}