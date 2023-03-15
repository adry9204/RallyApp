package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.HomeActivity
import com.example.rallyapp.dataModel.response_models.Menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivityViewModel: ViewModel() {

    companion object{
        private const val TAG = "HomeActivityViewModel"
    }

    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    var menuLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    fun getAllMenuItems(){
        GlobalScope.launch(Dispatchers.IO) {
            HomeActivity.menuRepo?.let { repo ->
                repo.getAllMenu {
                    menuLiveData.value = it
                }
            }
        }
    }

}