package com.example.rallyapp.utils

interface NetworkHandleCallback<T> {
    fun onConnected(data: T)
    fun onDisconnected()
}

interface SuccessFailureCallBack<T>{
    fun onSuccess(data: T)
    fun onFailure(data: T)
}