package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rallyapp.activities.HomeActivity
import com.example.rallyapp.api.dataModel.response_models.Menu
import kotlinx.coroutines.launch


class HomeActivityViewModel: ViewModel() {

    companion object{
        private const val TAG = "HomeActivityViewModel"
    }

    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    var menuLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    fun getAllMenuItems(){
        viewModelScope.launch {
            HomeActivity.menuRepo?.let { repo ->
                repo.getAllMenu {
                    menuLiveData.value = it
                }
            }
        }
    }

}