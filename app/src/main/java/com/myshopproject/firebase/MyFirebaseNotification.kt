package com.myshopproject.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.myshopproject.R
import com.myshopproject.domain.entities.FcmDataDomain
import com.myshopproject.domain.usecase.LocalUseCase
import com.myshopproject.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseNotification: FirebaseMessagingService() {

    @Inject
    lateinit var localUseCase: LocalUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(title: String?, messageBody: String?) {
        val contentIntent = Intent(applicationContext, LoginActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.product_logo)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val date = LocalDate.now().toString()
        CoroutineScope(Dispatchers.IO).launch {
            localUseCase.insertNotification(FcmDataDomain(notificationTitle = title.toString(), notificationBody = messageBody.toString(), notificationDate = date))

        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "Firebase Channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Firebase Notification"
    }
}