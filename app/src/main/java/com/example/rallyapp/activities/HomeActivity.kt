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
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Voucher
import com.example.rallyapp.recyclerview_adpaters.GridViewItemAdapter
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.databinding.ActivityHomeBinding
import com.example.rallyapp.databinding.FragmentHeaderBinding
import com.example.rallyapp.recyclerview_adpaters.CategoryListAdapter
import com.example.rallyapp.repo.MenuRepo
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.HomeActivityViewModel
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentBinding: FragmentHeaderBinding
    private var adapter: GridViewItemAdapter? = null
    private var categoryListAdapter: CategoryListAdapter ?= null
    private lateinit var viewModel: HomeActivityViewModel

    private var voucher: Voucher? = null

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


        //setting bottom nav listeners
        binding.newBottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_menu_item -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.search_menu_item -> {
                    // Respond to navigation item 2 click
                    var intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.carts_menu_item -> {
                    // Respond to navigation item 2 click
                    var intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.orders_menu_item -> {
                    // Respond to navigation item 2 click
                    var intent = Intent(this, OrdersActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.user_menu_item -> {
                    // Respond to navigation item 2 click
                    var intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.searchButton.setOnClickListener{

            Log.i(TAG, binding.searchTextField.editText?.text.toString() )
            val searchString = binding.searchTextField.editText?.text.toString()

            val searchActivityIntent = Intent(this, SearchActivity::class.java)
            val bundle = Bundle().apply {
                putString(SearchActivity.SEARCH_STRING, searchString)
            }
            searchActivityIntent.putExtras(bundle)
            startActivity(searchActivityIntent)
        }

        if(UserCredentials.isUserSet() && !UserCredentials.hasVoucher()){
            viewModel.getUsersVouchers(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
        }else{
            setVisibilityForHomeActivityHeader(View.INVISIBLE)
        }

        binding.homeActivityClaimVoucherButton.setOnClickListener {
            if(voucher != null){
                val alertManager = AlertManager(this)
                if(UserCredentials.hasVoucher()){
                    alertManager.showAlertWithOkButton(AlertData(
                        title = "You have a voucher",
                        message = "Your exisisting voucher will be replaced"
                    )){
                       claimVoucher()
                    }
                }else{
                   claimVoucher()
                }
            }
        }
        setListenerOnVoucherList()
    }

    private fun claimVoucher(){
        val alertManager = AlertManager(this)
        UserCredentials.setVoucher(voucher!!)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Voucher added",
            message = "voucher will be applied on checkout"
        ))
        voucher = null
        setVisibilityForHomeActivityHeader(View.INVISIBLE)
    }

    private fun setListenerOnVoucherList(){
        viewModel.voucherListLiveData.observe(this){
            if(it.isNotEmpty()){
                val random = Random.nextInt(0, it.size)
                voucher = it[random]
                setVoucherSection()
            }else{
                setVisibilityForHomeActivityHeader(View.INVISIBLE)
            }
        }
    }

    private fun setVoucherSection(){
        binding.homeActivityVoucherDiscountText.text = "${voucher!!.offerPercent}% of on code ${voucher!!.code}"
        setVisibilityForHomeActivityHeader(View.VISIBLE)
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
        if(voucher == null && visibility == View.VISIBLE){
            return
        }
        binding.searchButton.visibility = visibility
        binding.searchTextField.visibility = visibility
        binding.pizza.visibility = visibility
        binding.voucherSection.visibility = visibility

        if(visibility == View.VISIBLE){
            moveMenuDown()
        }else{
            moveMenuUp()
        }
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
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItemPosition > 0) {
                    setVisibilityForHomeActivityHeader(View.INVISIBLE)
                }

                if(!recyclerView.canScrollVertically(-1) && dy < -5){
                    setVisibilityForHomeActivityHeader(View.VISIBLE)
                }
            }
        })
    }

    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    fun goToOrders(v:View) {
        var intent = Intent(this, OrdersActivity::class.java)
        startActivity(intent)
    }
}