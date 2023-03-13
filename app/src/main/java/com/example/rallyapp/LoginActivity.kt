package com.example.rallyapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.dataModel.LoginRequest
import com.example.rallyapp.databinding.ActivityLoginBinding
import com.example.rallyapp.repo.UserRepo
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
        userRepo = UserRepo(applicationContext)

        binding.loginButton.setOnClickListener(){
            val userName = binding.userEmail.text.toString()
            val password = binding.userPassword.text.toString()

            val request = LoginRequest(userName, password)
            viewModel.loginUser(request)
            viewModel.userLoginListLiveData.observe(this) {
                it.forEach { it ->
                    if(it.message == "Login sucessful"){
                        Log.i(SingUpActivity.TAG,"User Login successful")
                        // Save fields to sharedPreferences
                        val sharedPreferences: SharedPreferences = this.getSharedPreferences("user_preferences",
                            Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("token", it.data.get(0).token)
                        editor.apply()

                        var intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        val view = binding.root
                        Snackbar.make(view, it.message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
            }
        }

        binding.signupButton.setOnClickListener(){
            var intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }

    }
}