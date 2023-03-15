package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.SearchActivity
import com.example.rallyapp.dataModel.response_models.Menu

class SearchActivityViewModel: ViewModel() {

    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    val menuListLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    fun searchMenuItem(searchString: String){
        SearchActivity.menuRepo?.let { repo ->
            repo.searchMenu(searchString){
                _menuListLiveData.value = it
            }
        }
    }

}