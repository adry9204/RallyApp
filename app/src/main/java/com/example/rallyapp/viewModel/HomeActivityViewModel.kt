package com.example.rallyapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rallyapp.activities.HomeActivity
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.Voucher
import com.example.rallyapp.repo.CategoryRepo
import com.example.rallyapp.repo.VoucherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivityViewModel(application: Application): AndroidViewModel(application) {

    companion object{
        private const val TAG = "HomeActivityViewModel"
    }

    private val categoryRepo = CategoryRepo(getApplication<Application>().applicationContext)
    private val voucherRepo = VoucherRepo()


    private var _menuListLiveData = MutableLiveData<List<Menu>>()
    var menuLiveData: MutableLiveData<List<Menu>> = _menuListLiveData

    private var _voucherListLiveData = MutableLiveData<List<Voucher>>()
    var voucherListLiveData: MutableLiveData<List<Voucher>> = _voucherListLiveData

    private var _categoryListLiveData = MutableLiveData<List<Category>>()
    var categoryListLiveData: MutableLiveData<List<Category>> = _categoryListLiveData

    fun getAllMenuItems(){
        viewModelScope.launch {
            HomeActivity.menuRepo?.let { repo ->
                repo.getAllMenu {
                    menuLiveData.value = it
                }
            }
        }
    }

    fun getAllCategories(){
        viewModelScope.launch {
            categoryRepo.getAllCategory {
                _categoryListLiveData.value = it
            }
        }
    }

    fun getMenuByCategory(categoryId: Int){
        viewModelScope.launch{
            HomeActivity.menuRepo?.let { menuRepo ->
                menuRepo.getMenuByCategory(categoryId){
                    _menuListLiveData.value = it
                }
            }
        }
    }

    fun getUsersVouchers(userId: Int, token: String){
        voucherRepo.getUsersVouchers(userId, token){
            _voucherListLiveData.value = it.data
        }
    }

}