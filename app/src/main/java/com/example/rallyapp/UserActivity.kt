package com.example.rallyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rallyapp.databinding.ActivityUserBinding


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = HeaderFragment.newInstance("do you want to edit your profile?")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        binding.saveChanges.setOnClickListener(){
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.logOutButton.setOnClickListener(){
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    //Auxiliar Fragments Methods
    //Header
    fun goToUserActivity(v: View) {
    }

    //Bottom Nav
    fun goToHome(v:View){
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(v:View){
        var intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
    fun goToCart(v:View){
        var intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }
}