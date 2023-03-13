package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.databinding.ActivityCartBinding
import com.example.rallyapp.databinding.ActivitySearchBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private var adapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting the header with the correct tagline
        var fragment: Fragment = HeaderFragment.newInstance("check your products in cart!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        //creating and filling the Recycler View
        val cardImages: Array<String> = resources.getStringArray(R.array.cardImages)
        val cardTitles: Array<String> = resources.getStringArray(R.array.cardTitles)
        val cardPrices: Array<String> = resources.getStringArray(R.array.cardPrices)

        val gridViewItem = findViewById<RecyclerView>(R.id.shopping_cart_recyclerview)
        gridViewItem.layoutManager = GridLayoutManager(this, 1)
        adapter = CartAdapter(cardImages, cardTitles, cardPrices)

        gridViewItem.adapter = adapter
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