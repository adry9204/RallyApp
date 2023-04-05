package com.example.rallyapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

data class AlertData(
    val title: String,
    val message: String
)

class AlertManager(val context: Context) {
    fun showAlertWithOkButton(alertData: AlertData, onClickOk: (()-> Unit) ?= null){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(alertData.title)
        alertDialogBuilder.setMessage(alertData.message)
        alertDialogBuilder.setPositiveButton("OK"){_, _ ->
            onClickOk?.let { it() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}