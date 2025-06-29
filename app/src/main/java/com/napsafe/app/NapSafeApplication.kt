package com.napsafe.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NapSafeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Location tracking channel
            val trackingChannel = NotificationChannel(
                LOCATION_TRACKING_CHANNEL_ID,
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Ongoing location tracking for alarms"
                setShowBadge(false)
            }
            
            // Alarm notification channel
            val alarmChannel = NotificationChannel(
                ALARM_NOTIFICATION_CHANNEL_ID,
                "Location Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Location alarm notifications"
                enableVibration(true)
                setShowBadge(true)
            }
            
            notificationManager.createNotificationChannels(listOf(trackingChannel, alarmChannel))
        }
    }
    
    companion object {
        const val LOCATION_TRACKING_CHANNEL_ID = "location_tracking"
        const val ALARM_NOTIFICATION_CHANNEL_ID = "alarm_notifications"
    }
}