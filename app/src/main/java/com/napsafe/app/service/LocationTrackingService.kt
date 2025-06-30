package com.napsafe.app.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.napsafe.app.NapSafeApplication
import com.napsafe.app.R
import com.napsafe.app.MainActivity

class LocationTrackingService : Service() {
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    
    // Adaptive interval: short if near alarm, long otherwise
    private var currentInterval: Long = LONG_INTERVAL

    // In-memory cache of active alarms (to be updated by ViewModel/Repository)
    companion object {
        private const val NOTIFICATION_ID = 1001
        // Google/Uber best practice: 2s for high-accuracy, 15s for background
        private const val SHORT_INTERVAL = 2000L // 2 seconds
        private const val LONG_INTERVAL = 15000L // 15 seconds

        // Static cache of active alarms
        private var activeAlarms: List<com.napsafe.app.data.model.LocationAlarm> = emptyList()
        fun setActiveAlarms(alarms: List<com.napsafe.app.data.model.LocationAlarm>) {
            activeAlarms = alarms
        }
    }

    // Check if current location is within any alarm's radius
    private fun isNearAnyAlarm(): Boolean {
        val lastLocation = lastKnownLocation ?: return false
        for (alarm in activeAlarms) {
            if (!alarm.isActive) continue
            val results = FloatArray(1)
            android.location.Location.distanceBetween(
                lastLocation.latitude, lastLocation.longitude,
                alarm.latitude, alarm.longitude, results
            )
            if (results[0] <= alarm.radius) {
                return true
            }
        }
        return false
    }

    private var lastKnownLocation: android.location.Location? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    lastKnownLocation = location
                    // Adaptive interval logic (Uber/Rapido/Google best practice)
                    val shouldUseShort = isNearAnyAlarm()
                    val newInterval = if (shouldUseShort) SHORT_INTERVAL else LONG_INTERVAL
                    if (newInterval != currentInterval) {
                        currentInterval = newInterval
                        restartLocationUpdates()
                    }
                }
            }
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_SERVICE") {
            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }
        val notification = createNotification()
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            // Use reflection for compatibility with lower compileSdk
            try {
                val serviceInfoClass = Class.forName("android.app.ServiceInfo")
                val typeField = serviceInfoClass.getField("FOREGROUND_SERVICE_TYPE_LOCATION")
                val typeValue = typeField.getInt(null)
                startForeground(NOTIFICATION_ID, notification, typeValue)
            } catch (e: Exception) {
                // Fallback: just use the old signature
                startForeground(NOTIFICATION_ID, notification)
            }
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        startLocationUpdates()
        return START_STICKY
    }
    
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            currentInterval // Adaptive interval
        ).build()
        
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }
    
    // Restart updates with new interval
    private fun restartLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        startLocationUpdates()
    }
    
    private fun createNotification(): Notification {
        // Intent to stop the service
        val stopIntent = Intent(this, LocationTrackingService::class.java).apply {
            action = "STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Intent to open the app when notification is tapped
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, NapSafeApplication.LOCATION_TRACKING_CHANNEL_ID)
            .setContentTitle("NapSafe is monitoring your location")
            .setContentText("Location tracking is active for alarms and safety.")
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppPendingIntent)
            .addAction(R.drawable.ic_notification, "Stop", stopPendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}