package com.example.rallyapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.databinding.ActivitySingleOrderBinding
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.recyclerview_adpaters.CartAdapter
import com.example.rallyapp.recyclerview_adpaters.GridViewItemAdapter
import com.example.rallyapp.recyclerview_adpaters.SingleOrderAdapter
import com.example.rallyapp.viewModel.HomeActivityViewModel

class SingleOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleOrderBinding
    private var adapter: SingleOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = HeaderFragment.newInstance("check out the details of this order!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()


        //creating and filling the Recycler View
        val cardImages: Array<String> = resources.getStringArray(R.array.cardImages)
        val cardTitles: Array<String> = resources.getStringArray(R.array.cardTitles)
        val cardPrices: Array<String> = resources.getStringArray(R.array.cardPrices)

        val gridViewItem = findViewById<RecyclerView>(R.id.single_order_recyclerview)
        gridViewItem.layoutManager = GridLayoutManager(this, 1)
        adapter = SingleOrderAdapter(cardImages, cardTitles, cardPrices)

        gridViewItem.adapter = adapter


    }


}