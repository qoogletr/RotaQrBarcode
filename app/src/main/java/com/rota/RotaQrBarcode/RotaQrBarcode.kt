package com.rota.RotaQrBarcode

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.rota.RotaQrBarcode.utils.PreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RotaBarcodeApp : Application() {

    companion object {
        const val SCANNER_NOTIFICATION_CHANNEL_ID = "scanner_notifications"
        lateinit var instance: RotaBarcodeApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        PreferencesManager.init(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val scannerChannel = NotificationChannel(
                SCANNER_NOTIFICATION_CHANNEL_ID,
                getString(R.string.scanner_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.scanner_notification_channel_description)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(scannerChannel)
        }
    }
}
