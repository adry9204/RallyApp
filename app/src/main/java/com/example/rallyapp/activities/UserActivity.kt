package com.example.rallyapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rallyapp.api.dataModel.request_models.UpdateUserRequest
import com.example.rallyapp.fragments.HeaderFragment
import com.example.rallyapp.databinding.ActivityUserBinding
import com.example.rallyapp.repo.UserRepo
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.MainActivityViewModel
import com.example.rallyapp.viewModel.UserActivityViewModel
import com.google.android.material.snackbar.Snackbar


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    companion object {
        var userRepo: UserRepo? = null
        const val TAG = "UserActivity"
        lateinit var viewModel: UserActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //view model to integrate Api call
        viewModel = ViewModelProvider(this)[UserActivityViewModel::class.java]
        userRepo = UserRepo(applicationContext)
        val view = binding.root

        var fragment: Fragment = HeaderFragment.newInstance("do you want to edit your profile?")
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()

        binding.logOutButton.setOnClickListener() {
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                "user_preferences",
                Context.MODE_PRIVATE
            )
            val token = sharedPreferences.getString("token", "defaultToken")
            if (token != null) {
                viewModel.logoutUser(token)
            }
            viewModel.userLogoutListLiveData.observe(this) {
                it.forEach { it ->
                    if (it.message == "Logged out successfully") {
                        Log.i(SingUpActivity.TAG, "User Logout successful")
                        var intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(view, it.message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
            }
        }

        getUserData()
        listenForAlertFromViewModel()

        binding.saveChanges.setOnClickListener {
            if(verifyInputs()){
                updateUser()
            }
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun getUserData() {
        if (UserCredentials.isUserSet()) {
            viewModel.getUserById(UserCredentials.getUserId()!!, UserCredentials.getToken()!!)
            viewModel.userData.observe(this) {
                binding.userName.editText!!.text = it.fullName!!.toEditable()
                binding.userEmail.editText!!.text = it.email!!.toEditable()
                binding.userUsername.editText!!.text = it.userName!!.toEditable()
            }
        } else {
            whenUserNotLoggedIn()
        }
    }

    private fun listenForAlertFromViewModel(){
        viewModel.showAlert.observe(this){
            val alertManager = AlertManager(this)
            alertManager.showAlertWithOkButton(it){}
        }
    }

    private fun whenUserNotLoggedIn(){
        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Login",
            message = "Please login before you can use this page"
        )) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showAlertForMissingField(fieldName: String){
        val alertManager = AlertManager(this)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Missing Fields",
            message = "Please enter a $fieldName before you can update"
        )) {}
    }

    private fun verifyInputs(): Boolean {
        if(binding.userName.editText!!.text.isEmpty()){
            showAlertForMissingField("first name and last name")
            return false
        }

        if(binding.userEmail.editText!!.text.isEmpty()){
            showAlertForMissingField("email")
            return false
        }

        if(binding.userUsername.editText!!.text.isEmpty()){
            showAlertForMissingField("username")
            return false
        }
        return true
    }


    private fun updateUser(){
        if(!UserCredentials.isUserSet()){
            whenUserNotLoggedIn()
            return
        }

        val userName = binding.userUsername.editText!!.text.toString()
        val name = binding.userName.editText!!.text.toString()
        val email = binding.userEmail.editText!!.text.toString()

        val password: String? = if(binding.userPassword.editText!!.text.toString().isEmpty()){
            null
        }else{
            binding.userPassword.editText!!.text.toString()
        }

        val updateRequest = UpdateUserRequest(
            userName = userName,
            fullName = name,
            email = email,
            password = password,
            userId = UserCredentials.getUserId()!!
        )
        viewModel.updateUser(updateRequest, UserCredentials.getToken()!!)
    }
}