package com.example.rallyapp.user

object UserCredentials {

    const val SHARED_PREFERENCE_NAME = "user_preferences"
    const val SHARED_PREFERENCE_TOKEN_KEY = "token"
    const val SHARED_PREFERENCE_USERID_KEY = "user_id"

    private var token: String? = null
    private var userId: Int? = null

    fun setToken(token: String){ this.token = "Bearer $token" }
    fun getToken(): String? { return this.token }

    fun setUserId(userId: Int){ this.userId = userId }
    fun getUserId(): Int? { return this.userId }

    fun isUserSet(): Boolean{
        if(this.token == null || this.userId == null){
            return false
        }
        return true
    }

}