package com.example.rallyapp

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.rallyapp.api_to_database.MenuApiToDatabase
import com.example.rallyapp.services.MenuLookupService
import com.example.rallyapp.utils.NotificationHelper


class AppInit : Application() {
    companion object{
        const val TAG = "AppInit"
    }

    override fun onCreate() {
        super.onCreate()
        if(!isServiceRunning(this, MenuLookupService::class.java)){
            Log.i(TAG, "startMenuLookupService is running")
            startMenuLookUpService()
        }

        val menuApiToDatabase = MenuApiToDatabase(this)
        menuApiToDatabase.loadCategoryFromServer()
        menuApiToDatabase.loadMenuFromServer()

        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()
    }

    private fun startMenuLookUpService(){
        val menuLookupServiceIntent = Intent(this, MenuLookupService::class.java)
        startService(menuLookupServiceIntent)
        Log.i(TAG, "Launching->startMenuLookupService")
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION") val services = manager.getRunningServices(Int.MAX_VALUE)
        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}