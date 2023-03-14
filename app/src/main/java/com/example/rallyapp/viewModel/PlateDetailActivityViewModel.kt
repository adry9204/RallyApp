package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rallyapp.PlateDetailActivity
import com.example.rallyapp.dataModel.Menu

class PlateDetailActivityViewModel: ViewModel() {

    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    val menuListLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    fun getMenuItemById(menuId: Int){
        PlateDetailActivity.menuRepo?.let { menuRepo ->
            menuRepo.getMenuItemById(menuId){
                _menuListLiveData.value = it
            }
        }
    }


}