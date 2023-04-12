package com.example.rallyapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.RegisterRequest
import com.example.rallyapp.databinding.ActivitySingUpBinding
import com.example.rallyapp.repo.UserRepo
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.utils.NetworkHelper
import com.example.rallyapp.viewModel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar

class SingUpActivity : AppCompatActivity() {

    //a comment
    private lateinit var binding: ActivitySingUpBinding
    companion object {
        var userRepo: UserRepo? = null
        const val TAG = "SingUpActivity"
        lateinit var viewModel: MainActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        userRepo = UserRepo(applicationContext)

        binding.createUserButton.setOnClickListener{


            val networkHelper = NetworkHelper(this)
            if(networkHelper.isConnectedToNetwork()){
                if(verifyInputs()){
                    val request = makeRequest()
                    showLoadingScreen()
                    viewModel.registerUser(request)
                }
            }else{
                val alertManager = AlertManager(this)
                alertManager.showAlertWithOkButton(AlertData(
                    title = "No internet connection",
                    message = ""
                ))
            }


            viewModel.userRegisterListLiveData.observe(this) {
                hideLoadingScreen()
                it.forEach { it ->
                    if(it.message == "Successfully added user to database"){
                        Log.i(TAG,"User registered")
                        val alertManager = AlertManager(this)
                        alertManager.showAlertWithOkButton(AlertData(
                            title = "Registration Successful",
                            message = "You can now log in to your account."
                        )){
                            var intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        val view = binding.root
                        Snackbar.make(view, it.message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    }
                }
            }
        }

        binding.backToLoginButton.setOnClickListener(){
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun makeRequest(): RegisterRequest {
        val name = binding.userName.text.toString()
        val userUsername = binding.userUsername.text.toString()
        val userEmail = binding.userEmail.text.toString()
        val userPassword = binding.userPassword.text.toString()

        return RegisterRequest(name, userUsername, userEmail, userPassword)
    }

    private fun verifyInputs(): Boolean{
        val name = binding.userName.text.toString()
        val userUsername = binding.userUsername.text.toString()
        val userEmail = binding.userEmail.text.toString()
        val userPassword = binding.userPassword.text.toString()

        if(name.isEmpty()){
            makeMissingFieldAlert("first name and a last name")
            return false
        }

        if(userUsername.isEmpty()){
            makeMissingFieldAlert("username")
            return false
        }

        if(userEmail.isEmpty()){
            makeMissingFieldAlert("email")
            return false
        }

        if(userPassword.isEmpty()){
            makeMissingFieldAlert("email")
            return false
        }
        return true
    }

    fun makeMissingFieldAlert(fieldName: String){
        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Missing information",
            message = "Please enter a $fieldName"
        ))
    }



    fun showLoadingScreen(){
        binding.signupActivityLoadingScreen.visibility = View.VISIBLE
    }

    fun hideLoadingScreen(){
        binding.signupActivityLoadingScreen.visibility = View.GONE
    }
}