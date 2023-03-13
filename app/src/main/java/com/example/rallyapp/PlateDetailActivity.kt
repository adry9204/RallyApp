package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.rallyapp.databinding.ActivityLoginBinding
import com.example.rallyapp.databinding.ActivityPlateDetailBinding

class PlateDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlateDetailBinding
    private  var quantity: Int = 1

    companion object{
        const val PLATE_TITLE = "title"
        const val PLATE_PRICE = "price"
        const val PLATE_DESCRIPTION = "description"
        const val PLATE_IMAGE = "image"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlateDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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