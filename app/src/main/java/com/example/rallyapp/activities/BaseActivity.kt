package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.MenuRes
import com.example.rallyapp.R
import com.example.rallyapp.databinding.ActivityBaseBinding
import com.example.rallyapp.databinding.ActivityHomeBinding

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    protected fun setMenu(@MenuRes selectedItem: Int): Boolean{
            return when(selectedItem) {
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
                    var intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.orders_menu_item -> {
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
}

enum class MenuOption {
    Home, Search, Orders, Profile, Cart
}