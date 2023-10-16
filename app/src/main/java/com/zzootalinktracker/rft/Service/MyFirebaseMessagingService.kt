package com.zzootalinktracker.rft.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zzootalinktracker.android.Ui.Activity.SplashActivity
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.MainActivity

const val channelId = "notification_channel"
const val channelName = "com.zzootalinktracker.rft"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
            var title = remoteMessage.notification!!.title
            var body = remoteMessage.notification!!.body
            Log.e("msg", title.toString())
            Log.e("body", body.toString())
        }
    }

    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews(packageName, R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.ron_finemore_logo)
        return remoteView
    }

    /*fun generateNotification(title: String, message: String) {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

            val builder = NotificationCompat.Builder(
                this,
                channelId
            )
                .setSmallIcon(R.drawable.ron_finemore_logo)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val iconCompat = IconCompat.createWithAdaptiveBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.ron_finemore_logo)
                )
                builder.setBubbleMetadata(
                    NotificationCompat.BubbleMetadata.Builder()
                        .setIntent(pendingIntent)
                        .setIcon(iconCompat)
                        .setAutoExpandBubble(true)
                        .build()
                )
            }
            builder.setContent(getRemoteView(title, message))
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                notificationManager.createNotificationChannel(notificationChannel)
            }
            notificationManager.notify(0, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
    fun generateNotification(title: String, message: String) {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

            val builder = NotificationCompat.Builder(
                this,
                channelId
            )
                .setSmallIcon(R.drawable.ron_finemore_logo)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                notificationManager.createNotificationChannel(notificationChannel)
            }
            notificationManager.notify(0, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

    }
}
