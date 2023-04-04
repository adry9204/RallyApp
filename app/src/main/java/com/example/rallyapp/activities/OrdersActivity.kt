package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.ActivityLoginBinding
import com.example.rallyapp.databinding.ActivityOrdersBinding
import com.example.rallyapp.fragments.HeaderFragment

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = HeaderFragment.newInstance("check out your past orders!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()
    }

    fun goToOrders(v: View) {
    }
}