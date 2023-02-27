package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.FragmentHeaderBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentBinding: FragmentHeaderBinding
    private var adapter: GridViewItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = Header.newInstance("time to eat!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        binding.pizza.bringToFront()

        //creating and filling the Recycler View
        val cardImages: Array<String> = resources.getStringArray(R.array.cardImages)
        val cardTitles: Array<String> = resources.getStringArray(R.array.cardTitles)
        val cardPrices: Array<String> = resources.getStringArray(R.array.cardPrices)

        val gridViewItem = findViewById<RecyclerView>(R.id.home_menu_recyclerview)
        gridViewItem.layoutManager = GridLayoutManager(this, 2)
        adapter = GridViewItemAdapter(cardImages, cardTitles, cardPrices)

        gridViewItem.adapter = adapter
    }

    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}