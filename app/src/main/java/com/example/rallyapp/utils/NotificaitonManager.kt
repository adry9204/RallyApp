package com.example.rallyapp.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.OrdersHelper
import com.example.rallyapp.api.dataModel.response_models.User
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import com.squareup.picasso.Target
import kotlinx.coroutines.*

class NotificationHelper(val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_NAME = "Rally App Notifications"
        const val NOTIFICATION_CHANNEL_DESCRIPTION =
            "notifications related to orders and items in cart"
        const val NOTIFICATION_CHANNEL_ID = "rally_notification_channel"

        var NOTIFICATION_ID = 1
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NOTIFICATION_CHANNEL_NAME
            val descriptionText = NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun displaySimpleNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_food_bank_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
        NOTIFICATION_ID++
    }

    @SuppressLint("MissingPermission")
    fun displayOrderStatusNotification(order: Order<User>, packageName: String) {
        val notificationLayout = RemoteViews(packageName, R.layout.order_status_notification)
        notificationLayout.setTextViewText(
            R.id.order_status_notification_title,
            "your order is ${OrdersHelper.getStatusFromOrder(order)}"
        )
        notificationLayout.setTextViewText(
            R.id.order_status_notification_item_list,
            "${order.orderDetails[0].menu.name.lowercase()}..+${order.orderDetails.size - 1} items"
        )
        notificationLayout.setTextViewText(R.id.order_status_notification_time, getCurrentDateTimeInFormat())

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.fork)
            .setContentTitle("Order status update")
            .setCustomContentView(notificationLayout)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
        NOTIFICATION_ID++
    }


    private fun getCurrentDateTimeInFormat(): String{
        val format = SimpleDateFormat("h:mma d MMM", Locale.getDefault())
        return format.format(Date())
    }

}