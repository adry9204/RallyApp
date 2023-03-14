package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SearchActivity"
        const val SEARCH_STRING = "search_string"
    }

    private lateinit var binding: ActivitySearchBinding
    private var adapter: GridViewItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        bundle?.let {
           val searchString =  it.getString(SEARCH_STRING, "")
           Log.i(TAG, searchString)
        }

        //setting the header with the correct tagline
        var fragment: Fragment = HeaderFragment.newInstance("what are you looking for?")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        //creating and filling the Recycler View
        val cardImages: Array<String> = resources.getStringArray(R.array.cardImages)
        val cardTitles: Array<String> = resources.getStringArray(R.array.cardTitles)
        val cardPrices: Array<String> = resources.getStringArray(R.array.cardPrices)

        val gridViewItem = findViewById<RecyclerView>(R.id.search_menu_recyclerview)
        gridViewItem.layoutManager = GridLayoutManager(this, 2)
//        adapter = GridViewItemAdapter(cardImages, cardTitles, cardPrices)

//        gridViewItem.adapter = adapter
    }

    //takes users to Detail Activity when tap on recycler item
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
    fun goToHome(v:View){
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(v:View){
    }
    fun goToCart(v:View){
        var intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

}