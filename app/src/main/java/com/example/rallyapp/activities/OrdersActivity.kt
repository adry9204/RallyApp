package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.ActivityLoginBinding
import com.example.rallyapp.databinding.ActivityOrdersBinding
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.recyclerview_adpaters.OrdersAdapter
import com.example.rallyapp.recyclerview_adpaters.SingleOrderAdapter

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private var adapter: OrdersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = HeaderFragment.newInstance("check out your past orders!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        //creating and filling the Recycler View
        val cardDate: Array<String> = resources.getStringArray(R.array.cardDates)
        val cardSummary: Array<String> = resources.getStringArray(R.array.cardSummary)


        val gridViewItem = findViewById<RecyclerView>(R.id.orders_recyclerview)
        gridViewItem.layoutManager = GridLayoutManager(this, 1)
        adapter = OrdersAdapter(cardSummary, cardDate)

        gridViewItem.adapter = adapter
    }

    fun goToOrders(v: View) {
    }
    fun goToSingleOrder(v: View) {
        val intent = Intent(this, SingleOrderActivity::class.java)
        startActivity(intent)
    }
}