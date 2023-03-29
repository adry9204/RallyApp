package com.example.rallyapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.databinding.ActivityUserBinding
import com.example.rallyapp.repo.UserRepo
import com.example.rallyapp.viewModel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    companion object {
        var userRepo: UserRepo? = null
        const val TAG = "UserActivity"
        lateinit var viewModel: MainActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //view model to integrate Api call
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        userRepo = UserRepo(applicationContext)
        val view = binding.root

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
            val sharedPreferences: SharedPreferences = this.getSharedPreferences("user_preferences",
                Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token","defaultToken")
            if (token != null) {
                viewModel.logoutUser(token)
            }
            viewModel.userLogoutListLiveData.observe(this) {
                it.forEach { it ->
                    if(it.message == "Logged out successfully"){
                        Log.i(SingUpActivity.TAG,"User Logout successful")
                        var intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(view, it.message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
            }
        }

    }
}