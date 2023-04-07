package com.example.rallyapp.repo

import com.example.rallyapp.api.api_helpers.AddressApiHelper
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.User

class AddressRepo {

    private val addressApiHelper =AddressApiHelper()

    fun getUsersAddress(userId: Int, token: String, callback: (ApiResponse<Address<User>>)-> Unit){
        addressApiHelper.getUsersAddress(userId, token){
            callback(it)
        }
    }
}