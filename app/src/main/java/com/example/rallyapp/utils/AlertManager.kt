package com.example.rallyapp.utils

import android.content.Context
import android.widget.Toast
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

    fun makeToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}