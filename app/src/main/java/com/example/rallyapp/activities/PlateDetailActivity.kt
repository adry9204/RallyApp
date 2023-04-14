package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.databinding.ActivityPlateDetailBinding
import com.example.rallyapp.repo.CartRepo
import com.example.rallyapp.repo.MenuRepo
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.PlateDetailActivityViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class PlateDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityPlateDetailBinding
    private  var quantity: Int = 1
    private  lateinit var viewModel: PlateDetailActivityViewModel
    private  lateinit var menu: Menu

    companion object{
        const val TAG = "PlateDetailActivity"

        const val MENU_ITEM_ID = "menu_item_id"

        var menuRepo: MenuRepo ?= null
        var cartRepo: CartRepo ?= null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlateDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuRepo = MenuRepo(this)
        cartRepo = CartRepo(this)
        viewModel = ViewModelProvider(this)[PlateDetailActivityViewModel::class.java]

        val menuId = getMenuItemId()
        viewModel.getMenuItemById(menuId)

        binding.plusButton.setOnClickListener{
            quantity++
            binding.quantityLabel.text = "Qty: " + quantity.toString()
        }

        binding.minusButton.setOnClickListener{
            if (quantity > 1) {
                quantity--
                binding.quantityLabel.text = "Qty: " + quantity.toString()
            }
        }

        binding.addToCartButton.setOnClickListener {
            showLoadingScreen()
            viewModel.addItemToCart(
                userId = UserCredentials.getUserId() ?: 0,
                menuId = menu.id,
                quantity = cleanQuantityString(binding.quantityLabel.text.toString()),
                authorizationToken = UserCredentials.getToken()!!
            )
        }

        listenForAlertFromViewModel()
        listenForAddToCartResponse()
        setObserverOnMenuData()

        //setting bottom nav listeners
        binding.newBottomNav.selectedItemId = R.id.carts_menu_item
        binding.newBottomNav.setOnNavigationItemSelectedListener {
            setMenu(it.itemId)
        }

    }

    private fun cleanQuantityString(quantityString: String): Int {
        val cleanedString = quantityString.substring(5) // Remove "Qty: " prefix
        return cleanedString.toInt()
    }

    private fun listenForAddToCartResponse(){
        viewModel.addCartResponseLiveData.observe(this){
            hideLoading()
            if(it.success == 1){
                makeSnackBar("Item added to cart Successfully", binding.addToCartButton)
            }else{
                makeSnackBar("Failed to add Item to Cart", binding.addToCartButton)
            }
        }
    }

    private fun makeSnackBar(message: String, view: View) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun setObserverOnMenuData(){
        viewModel.menuListLiveData.observe(this){
            hideLoading()
            Log.i(TAG, it.toString())
            if(it.isNotEmpty()){
                binding.plateDescription.text = it[0].description.lowercase()
                binding.platePrice.text = "$${it[0].price}"
                binding.plateTitle.text = it[0].name.lowercase()
                Picasso.get().load(it[0].image).into(binding.plateImage)
                menu = it[0]
            }
        }
    }

    private fun listenForAlertFromViewModel(){
        viewModel.showAlert.observe(this){
            hideLoading()
            val alertHelper = AlertManager(this)
            alertHelper.showAlertWithOkButton(it)
        }
    }

    private fun getMenuItemId(): Int{
        val menuId = intent.extras?.let {
            it.getInt(MENU_ITEM_ID, 0)
        } ?: 0
        Log.i(TAG, menuId.toString())
        return menuId
    }

    private fun showLoadingScreen(){
        binding.plateDetailActivityLoadingScreen.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.plateDetailActivityLoadingScreen.visibility = View.GONE
    }

    fun goToOrders(v:View) {
        var intent = Intent(this, OrdersActivity::class.java)
        startActivity(intent)
    }
}