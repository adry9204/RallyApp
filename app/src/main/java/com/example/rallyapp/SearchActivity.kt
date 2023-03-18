package com.example.rallyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rallyapp.databinding.ActivitySearchBinding
import com.example.rallyapp.repo.MenuRepo
import com.example.rallyapp.viewModel.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SearchActivity"
        const val SEARCH_STRING = "search_string"

        var menuRepo: MenuRepo? = null
    }

    private lateinit var binding: ActivitySearchBinding
    private var adapter: GridViewItemAdapter? = null
    private lateinit var viewModel: SearchActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuRepo = MenuRepo(this)
        viewModel = ViewModelProvider(this)[SearchActivityViewModel::class.java]

        val bundle = intent.extras
        bundle?.let {
            val searchString =  it.getString(SEARCH_STRING, "")
            binding.searchTextFieldSearchScreen.editText?.text = searchString.toEditable()
            viewModel.searchMenuItem(searchString)
        }

        //setting the header with the correct tagline
        var fragment: Fragment = HeaderFragment.newInstance("what are you looking for?")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        binding.searchButtonSearchScreen.setOnClickListener{
            val searchString = binding.searchTextFieldSearchScreen.editText?.let{
                it.text.toString()
            } ?: ""
            viewModel.searchMenuItem(searchString)
            binding.searchTextFieldSearchScreen.clearFocus()
            hideKeyboard(binding.searchTextFieldSearchScreen)
        }

       setObserverForData()

    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun hideKeyboard(view: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setObserverForData(){
        viewModel.menuListLiveData.observe(this){
            binding.searchMenuRecyclerview.layoutManager = GridLayoutManager(this, 2)
            adapter = GridViewItemAdapter(this, it)
            binding.searchMenuRecyclerview.adapter = adapter
        }
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