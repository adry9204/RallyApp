package com.example.rallyapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.api.dataModel.LoginRequest
import com.example.rallyapp.databinding.ActivityLoginBinding
import com.example.rallyapp.repo.UserRepo
import com.example.rallyapp.services.CartBackgroundService
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.utils.NetworkHelper
import com.example.rallyapp.viewModel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    companion object {
        var userRepo: UserRepo? = null
        const val TAG = "LoginActivity"
        lateinit var viewModel: MainActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        userRepo = UserRepo(this)

        binding.loginButton.setOnClickListener{
            val networkHelper = NetworkHelper(this)
            if(networkHelper.isConnectedToNetwork()){
                val userName = binding.userEmail.text.toString()
                val password = binding.userPassword.text.toString()

                val request = LoginRequest(userName, password)
                viewModel.loginUser(request)

            }else{
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "Cant Login",
                    message = "Cannot login without internet"
                ))
            }

        }

        binding.signupButton.setOnClickListener(){
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }

        setObserverForLoginResponse()

    }

    private fun setObserverForLoginResponse(){
        viewModel.userLoginListLiveData.observe(this) {
            it.forEach { it ->
                if(it.success == 1){
                    Log.i(SingUpActivity.TAG,"User Login successful")

                    UserCredentials.setUserCredentials(
                        this,
                        userId = it.data[0].userId,
                        token = it.data[0].token,
                        userName = it.data[0].userName
                    )
                    startCartService()
                    goToHomeScreen()
                } else {
                    val alertManager = AlertManager(this)
                    val alertData = AlertData(
                        title = "Login Failed",
                        message = it.message
                    )
                    alertManager.showAlertWithOkButton(alertData)
                }
            }
        }
    }


    private fun startCartService(){
        val bundle = Bundle().apply {
            putInt(CartBackgroundService.CMD_KEY, CartBackgroundService.CMD_USER_LOGGED_IN)
        }
        val cartService = Intent(this, CartBackgroundService::class.java)
        cartService.putExtras(bundle)
        startService(cartService)
    }

    private fun goToHomeScreen(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}