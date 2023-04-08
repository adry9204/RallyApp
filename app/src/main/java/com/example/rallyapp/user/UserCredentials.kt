package com.example.rallyapp.user

import android.content.Context
import android.content.SharedPreferences
import com.example.rallyapp.api.dataModel.response_models.Voucher

object UserCredentials {

    const val SHARED_PREFERENCE_NAME = "user_preferences"
    const val SHARED_PREFERENCE_TOKEN_KEY = "token"
    const val SHARED_PREFERENCE_USERID_KEY = "user_id"

    private var token: String? = null
    private var userId: Int? = null
    private var userName: String? = null
    private var claimedVoucher: Voucher? = null

    fun setToken(token: String){ this.token = "Bearer $token" }
    fun getToken(): String? { return this.token }

    fun setUserId(userId: Int){ this.userId = userId }
    fun getUserId(): Int? { return this.userId }

    fun setUserName(userName: String){ this.userName = userName }
    fun getUserName(): String? { return this.userName }

    fun isUserSet(): Boolean{
        if(this.token == null || this.userId == null){
            return false
        }
        return true
    }

    fun hasVoucher(): Boolean {  return claimedVoucher != null }
    fun setVoucher(voucher: Voucher){ claimedVoucher = voucher }
    fun getVoucher(): Voucher? { return claimedVoucher }

    fun setUserCredentials(
        context: Context,
        userId: Int,
        token: String,
        userName: String
    ){
        val bearerToken = "Bearer $token"

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE)

        sharedPreferences.edit().apply {
            putString(SHARED_PREFERENCE_TOKEN_KEY, bearerToken)
            putInt(SHARED_PREFERENCE_USERID_KEY, userId)
            apply()
        }

        setUserId(userId)
        setToken(token)
        setUserName(userName)
    }

}