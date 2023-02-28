package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rallyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener(){
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener(){
            var intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }

    }
}