package com.example.rallyapp.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.recyclerview_adpaters.GridViewItemAdapter
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.FragmentHeaderBinding
import com.example.rallyapp.recyclerview_adpaters.CategoryListAdapter
import com.example.rallyapp.repo.MenuRepo
import com.example.rallyapp.viewModel.HomeActivityViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentBinding: FragmentHeaderBinding
    private var adapter: GridViewItemAdapter? = null
    private var categoryListAdapter: CategoryListAdapter ?= null
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
        setScrollListenerOnMenu()


        setObserverForMenuData()
        setupCategoryListRecyclerView()
        viewModel.getAllCategories()
        setObserversForCategoryData()


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

    private fun setupCategoryListRecyclerView(){
        binding.homeActivityCategoryRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)
        categoryListAdapter = CategoryListAdapter(this, viewModel, mutableListOf())
        binding.homeActivityCategoryRecyclerView.adapter = categoryListAdapter
    }

    private fun setObserversForCategoryData(){
        viewModel.categoryListLiveData.observe(this){
            Log.i(TAG, it.toString())
            categoryListAdapter?.let { categoryListAdapter ->
                categoryListAdapter.setCategoryItems(it)
                categoryListAdapter.notifyDataSetChanged()
            }
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

    private fun setVisibilityForHomeActivityHeader(visibility: Int){
        binding.searchButton.visibility = visibility
        binding.searchTextField.visibility = visibility
        binding.pizza.visibility = visibility
        binding.voucherSection.visibility = visibility
    }

    private fun moveMenuUp(){
        val layoutParams = binding.homeActivityCategoryRecyclerView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = binding.fragmentContainer.id
        binding.homeActivityCategoryRecyclerView.layoutParams = layoutParams
    }

    private fun moveMenuDown(){
        val layoutParams = binding.homeActivityCategoryRecyclerView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = binding.voucherSection.id
        binding.homeActivityCategoryRecyclerView.layoutParams = layoutParams
    }

    private fun setScrollListenerOnMenu(){
        binding.homeMenuRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var atTop = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItemPosition > 0) {
                    setVisibilityForHomeActivityHeader(View.INVISIBLE)
                    moveMenuUp()
                }

                if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    setVisibilityForHomeActivityHeader(View.VISIBLE)
                    moveMenuDown()
                    atTop = true
                }
//                if(!recyclerView.canScrollVertically(-1) && dy < 0 && atTop){
//                    setVisibilityForHomeActivityHeader(View.VISIBLE)
//                    moveMenuDown()
//                }

            }
        })
    }

    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}