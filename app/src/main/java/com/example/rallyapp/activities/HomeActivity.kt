package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rallyapp.recyclerview_adpaters.GridViewItemAdapter
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.FragmentHeaderBinding
import com.example.rallyapp.repo.MenuRepo
import com.example.rallyapp.viewModel.HomeActivityViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentBinding: FragmentHeaderBinding
    private var adapter: GridViewItemAdapter? = null
    private lateinit var viewModel: HomeActivityViewModel

    companion object {
        private const val TAG = "HomeActivity"
        var menuRepo: MenuRepo? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuRepo = MenuRepo(this)

        viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]
        viewModel.getAllMenuItems()



        var fragment: Fragment = HeaderFragment.newInstance("time to eat!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        binding.pizza.bringToFront()

        binding.homeMenuRecyclerview.layoutManager = GridLayoutManager(this, 2)
        adapter = GridViewItemAdapter(this, listOf())
        binding.homeMenuRecyclerview.adapter = adapter

        setObserverForMenuData()

        binding.searchButton.setOnClickListener{

            Log.i(TAG, binding.searchTextField.editText?.text.toString() + " heyyyyy ")
            val searchString = binding.searchTextField.editText?.text.toString()

            val searchActivityIntent = Intent(this, SearchActivity::class.java)
            val bundle = Bundle().apply {
                putString(SearchActivity.SEARCH_STRING, searchString)
            }
            searchActivityIntent.putExtras(bundle)
            startActivity(searchActivityIntent)
        }

    }

    private fun setObserverForMenuData(){
        viewModel.menuLiveData.observe(this) {
            adapter?.let{ adapter->
                adapter.setMenuItems(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}