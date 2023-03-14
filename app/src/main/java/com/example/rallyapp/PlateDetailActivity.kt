package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.databinding.ActivityPlateDetailBinding
import com.example.rallyapp.repo.MenuRepo
import com.example.rallyapp.viewModel.PlateDetailActivityViewModel

class PlateDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlateDetailBinding
    private  var quantity: Int = 1
    private  lateinit var viewModel: PlateDetailActivityViewModel

    companion object{
        const val TAG = "PlateDetailActivity"
        const val PLATE_TITLE = "title"
        const val PLATE_PRICE = "price"
        const val PLATE_DESCRIPTION = "description"
        const val PLATE_IMAGE = "image"

        const val MENU_ITEM_ID = "menu_item_id"

        var menuRepo: MenuRepo ?= null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlateDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuRepo = MenuRepo(this)
        viewModel = ViewModelProvider(this)[PlateDetailActivityViewModel::class.java]

        val menuId = getMenuItemId()
        viewModel.getMenuItemById(menuId)

        binding.plusButton.setOnClickListener(){
            quantity++
            binding.quantityLabel.text = "Qty: " + quantity.toString()
        }

        binding.minusButton.setOnClickListener(){
            if (quantity > 1) {
                quantity--
                binding.quantityLabel.text = "Qty: " + quantity.toString()
            }
        }

        setObserverOnMenuData()

    }

    private fun setObserverOnMenuData(){
        viewModel.menuListLiveData.observe(this){
            Log.i(TAG, it.toString())
            if(it.isNotEmpty()){
                binding.plateDescription.text = it[0].description.lowercase()
                binding.platePrice.text = "$${it[0].price}"
                binding.plateTitle.text = it[0].name.lowercase()
            }
        }
    }

    private fun getMenuItemId(): Int{
        val menuId = intent.extras?.let {
            it.getInt(MENU_ITEM_ID, 0)
        } ?: 0
        Log.i(TAG, menuId.toString())
        return menuId
    }

    //Auxiliar Fragments Methods
    //Header
    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    //Bottom Nav
    fun goToHome(v:View){
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


    fun goToSearch(v:View){
        var intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    fun goToCart(v:View){
        var intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }
}