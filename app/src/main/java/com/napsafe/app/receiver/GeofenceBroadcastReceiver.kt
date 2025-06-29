package com.napsafe.app.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.napsafe.app.NapSafeApplication
import com.napsafe.app.R
import kotlinx.coroutines.*

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GeofenceReceiver", "Geofence broadcast received")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            Log.e("GeofenceReceiver", "Geofencing error: ${geofencingEvent.errorCode}")
            return
        }

        val geofenceTransition = geofencingEvent?.geofenceTransition
        Log.d("GeofenceReceiver", "Geofence transition: $geofenceTransition")

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            Log.d("GeofenceReceiver", "Triggered geofences count: ${triggeringGeofences?.size}")

            triggeringGeofences?.forEach { geofence ->
                Log.d("GeofenceReceiver", "Triggering alarm for geofence: ${geofence.requestId}")
                triggerAlarm(context, geofence.requestId)
            }
        }
    }

    private fun triggerAlarm(context: Context, alarmId: String) {
        Log.d("GeofenceReceiver", "Triggering alarm for ID: $alarmId")

        // Get user preferences
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
        val soundEnabled = prefs.getBoolean("sound_enabled", true)
        val vibrationEnabled = prefs.getBoolean("vibration_enabled", true)

        Log.d("GeofenceReceiver", "Settings - Notifications: $notificationsEnabled, Sound: $soundEnabled, Vibration: $vibrationEnabled")

        // Launch coroutine for async operations
        scope.launch {
            try {
                // Show notification if enabled
                if (notificationsEnabled) {
                    showNotification(context, alarmId)
                }

                // Play sound if enabled
                if (soundEnabled) {
                    playAlarmSound(context)
                }

                // Vibrate if enabled
                if (vibrationEnabled) {
                    vibrateDevice(context)
                }

                Log.d("GeofenceReceiver", "Alarm triggered successfully for $alarmId")

            } catch (e: Exception) {
                Log.e("GeofenceReceiver", "Error triggering alarm", e)
            }
        }
    }

    private fun showNotification(context: Context, alarmId: String) {
        try {
            Log.d("GeofenceReceiver", "Showing notification for alarm: $alarmId")

            val notification = NotificationCompat.Builder(context, NapSafeApplication.ALARM_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("🚨 Location Alarm Triggered!")
                .setContentText("You've arrived at your destination!")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_MAX) // Maximum priority
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Sound, vibration, lights
                .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500)) // Custom vibration pattern
                .setLights(0xFF0000FF.toInt(), 1000, 1000) // Blue light
                .setOngoing(false)
                .setTimeoutAfter(30000) // Auto-dismiss after 30 seconds
                .build()

            val notificationManager = NotificationManagerCompat.from(context)

            // Check if notifications are enabled at system level
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(alarmId.hashCode(), notification)
                Log.d("GeofenceReceiver", "Notification shown successfully")
            } else {
                Log.w("GeofenceReceiver", "Notifications are disabled at system level")
            }

        } catch (e: Exception) {
            Log.e("GeofenceReceiver", "Failed to show notification", e)
        }
    }

    private suspend fun playAlarmSound(context: Context) = withContext(Dispatchers.Main) {
        try {
            Log.d("GeofenceReceiver", "Playing alarm sound")

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            // Request audio focus for alarm
            val audioFocusRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .build()
            } else null

            // Request audio focus
            val focusResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest != null) {
                audioManager.requestAudioFocus(audioFocusRequest)
            } else {
                @Suppress("DEPRECATION")
                audioManager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
                )
            }

            if (focusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // Get alarm sound URI
                val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

                val ringtone = RingtoneManager.getRingtone(context, alarmUri)

                if (ringtone != null) {
                    // Set audio attributes for alarm
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ringtone.audioAttributes = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    }

                    ringtone.play()
                    Log.d("GeofenceReceiver", "Alarm sound started")

                    // Stop sound after 10 seconds to prevent infinite playing
                    delay(10000)

                    if (ringtone.isPlaying) {
                        ringtone.stop()
                        Log.d("GeofenceReceiver", "Alarm sound stopped after timeout")
                    }

                    // Release audio focus
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest != null) {
                        audioManager.abandonAudioFocusRequest(audioFocusRequest)
                    } else {
                        @Suppress("DEPRECATION")
                        audioManager.abandonAudioFocus(null)
                    }
                } else {
                    Log.w("GeofenceReceiver", "Could not get ringtone for alarm sound")
                }
            } else {
                Log.w("GeofenceReceiver", "Could not gain audio focus for alarm")
            }

        } catch (e: Exception) {
            Log.e("GeofenceReceiver", "Failed to play alarm sound", e)
        }
    }

    private fun vibrateDevice(context: Context) {
        try {
            Log.d("GeofenceReceiver", "Starting device vibration")

            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (vibrator.hasVibrator()) {
                // Create strong vibration pattern for alarm
                val pattern = longArrayOf(
                    0,    // Start immediately
                    800,  // Vibrate for 800ms
                    200,  // Pause for 200ms
                    800,  // Vibrate for 800ms
                    200,  // Pause for 200ms
                    800,  // Vibrate for 800ms
                    500   // Final pause
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Use VibrationEffect for newer devices
                    val vibrationEffect = VibrationEffect.createWaveform(
                        pattern,
                        intArrayOf(0, 255, 0, 255, 0, 255, 0), // Amplitudes (max intensity)
                        -1 // Don't repeat
                    )

                    val audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()

                    vibrator.vibrate(vibrationEffect, audioAttributes)
                } else {
                    // Fallback for older devices
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(pattern, -1)
                }

                Log.d("GeofenceReceiver", "Device vibration started")
            } else {
                Log.w("GeofenceReceiver", "Device does not support vibration")
            }

        } catch (e: Exception) {
            Log.e("GeofenceReceiver", "Failed to vibrate device", e)
        }
    }
}