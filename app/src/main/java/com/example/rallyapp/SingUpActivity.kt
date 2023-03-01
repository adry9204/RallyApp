package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.dataModel.RegisterRequest
import com.example.rallyapp.databinding.ActivitySingUpBinding
import com.example.rallyapp.repo.UserRepo
import com.example.rallyapp.viewModel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar

class SingUpActivity : AppCompatActivity() {

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

        binding.createUserButton.setOnClickListener(){
            val userName = binding.userName.text.toString()
            val userUsername = binding.userUsername.text.toString()
            val userEmail = binding.userEmail.text.toString()
            val userPassword = binding.userPassword.text.toString()

            val request = RegisterRequest(userName, userUsername, userEmail, userPassword)
            viewModel.registerUser(request)
            viewModel.userRegisterListLiveData.observe(this) {
                it.forEach { it ->
                    if(it.message == "Successfully added user to database"){
                        Log.i(TAG,"User registered")
                        var intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
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
}