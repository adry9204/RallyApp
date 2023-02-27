package com.example.rallyapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rallyapp.databinding.ActivityUserBinding
import com.example.rallyapp.databinding.FragmentHeaderBinding


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragment: Fragment = Header.newInstance("do you want to edit your profile?")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        binding.saveChanges.setOnClickListener(){
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun goToUserActivity(v: View) {

    }
}