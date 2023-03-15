package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rallyapp.dataModel.response_models.Cart
import com.example.rallyapp.databinding.ActivityCartBinding
import com.example.rallyapp.repo.CartRepo
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.viewModel.CartActivityViewModel

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

    }

    private fun setObserverOnCartData(){
        viewModel.cartLiveData.observe(this){
            binding.shoppingCartRecyclerview.layoutManager = GridLayoutManager(this, 1)
            adapter = CartAdapter(it)
            binding.shoppingCartRecyclerview.adapter = adapter
            val totalCartPrice = calculateTotalFromCart(it)
            binding.totalAmountLabelCart.text = "$$totalCartPrice"
        }
    }

    private fun calculateTotalFromCart(cartItems: List<Cart>): Float{
        var totalPrice = 0.0f
        for (cartItem in cartItems){
            totalPrice += cartItem.price.toFloat()
        }
        return totalPrice
    }

    private fun gotToLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    //method for going to the detail view of a plate
    fun productCardViewOnClick(v:View) {
        if (v.id == R.id.recycler_card_view) {
            val i = Intent(this, PlateDetailActivity::class.java)
            startActivity(i)
        }
    }

    //Auxiliar Fragments Methods
    //Header
    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    //Bottom Nav
    fun goToHome(v: View){
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(v: View){
        var intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
    fun goToCart(v: View){
    }
}