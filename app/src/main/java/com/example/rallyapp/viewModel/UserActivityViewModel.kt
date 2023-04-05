package com.example.rallyapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rallyapp.activities.UserActivity
import com.example.rallyapp.api.dataModel.LogoutResponse
import com.example.rallyapp.api.dataModel.request_models.UpdateUserRequest
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.repo.UserRepo
import com.example.rallyapp.utils.AlertData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepo(getApplication<Application>().applicationContext)

    private var _userData = MutableLiveData<User>()
    val userData: MutableLiveData<User> = _userData

    private val _showAlert = MutableLiveData<AlertData>()
    val showAlert: LiveData<AlertData> = _showAlert

    private var _userLogoutListLiveData = MutableLiveData<List<LogoutResponse>>()
    var userLogoutListLiveData: MutableLiveData<List<LogoutResponse>> = _userLogoutListLiveData

    fun getUserById(userId: Int, token: String){
        CoroutineScope(Dispatchers.IO).launch{
            userRepo.getUserById(userId, token){
                if(it.success == 1 && (0 in 0..it.data.lastIndex)){
                    _userData.value = it.data[0]
                }else{
                    _showAlert.value = AlertData(
                        title =  "There was a problem getting data",
                        message = it.message
                    )
                }
            }
        }
    }

    fun updateUser(user: UpdateUserRequest, token: String){
        CoroutineScope(Dispatchers.IO).launch {
            userRepo.updateUser(user, token){
                val title : String = if(it.success == 1){
                    "Updated"
                }else{
                    "Problem Updating"
                }
                _showAlert.value = AlertData(
                    title = title,
                    message = it.message
                )
            }
        }
    }

    fun logoutUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = UserActivity.userRepo?.logoutUser("Bearer " + token)
        }
    }

    fun setLogoutData(list: List<LogoutResponse>){
        userLogoutListLiveData.value = list
    }

}