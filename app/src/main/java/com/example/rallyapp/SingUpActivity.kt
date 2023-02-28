package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rallyapp.databinding.ActivitySingUpBinding

class SingUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createUserButton.setOnClickListener(){
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.backToLoginButton.setOnClickListener(){
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}