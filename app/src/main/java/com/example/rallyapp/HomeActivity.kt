package com.example.rallyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.rallyapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = Header.newInstance("time to eat!")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()


    }

    fun goToUserActivity(v: View) {
        var intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}