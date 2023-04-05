package com.example.rallyapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.activities.SingUpActivity
import com.example.rallyapp.activities.UserActivity
import com.example.rallyapp.api.dataModel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * MainActivityViewModel class
 * View model to set User live data
 * author: Satender Yadav
 */
class MainActivityViewModel : ViewModel() {

    private var _userRegisterListLiveData = MutableLiveData<List<RegisterResponse>>()
    var userRegisterListLiveData: MutableLiveData<List<RegisterResponse>> = _userRegisterListLiveData

    private var _userLoginListLiveData = MutableLiveData<List<LoginResponse>>()
    var userLoginListLiveData: MutableLiveData<List<LoginResponse>> = _userLoginListLiveData

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = SingUpActivity.userRepo?.registerUser(request)
        }
    }

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = LoginActivity.userRepo?.loginUser(request)
        }
    }


    fun setRegisterData(list: List<RegisterResponse>){
        userRegisterListLiveData.value = list
    }

    fun setLoginData(list: List<LoginResponse>){
        userLoginListLiveData.value = list
    }


}