package com.napsafe.app.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.receiver.GeofenceBroadcastReceiver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofenceManager @Inject constructor(
    private val context: Context
) {
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun addGeofence(alarm: LocationAlarm) {
        try {
            // PRECISE radius calculation - ensure exact match with map display
            val preciseRadiusInMeters = alarm.radius // Already in meters from database

            Log.d("GeofenceManager", "Adding geofence for alarm: ${alarm.name}")
            Log.d("GeofenceManager", "Location: ${alarm.latitude}, ${alarm.longitude}")
            Log.d("GeofenceManager", "Radius: ${preciseRadiusInMeters}m (${preciseRadiusInMeters/1000f}km)")

            val geofence = Geofence.Builder()
                .setRequestId(alarm.id)
                .setCircularRegion(
                    alarm.latitude,
                    alarm.longitude,
                    preciseRadiusInMeters // EXACT radius in meters
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setLoiteringDelay(1000) // 1 second delay to avoid false triggers
                .build()

            val geofencingRequest = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()

            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener {
                    Log.d("GeofenceManager", "Geofence added successfully for ${alarm.name}")
                }
                .addOnFailureListener { exception ->
                    Log.e("GeofenceManager", "Failed to add geofence for ${alarm.name}", exception)
                }
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Exception adding geofence", e)
        }
    }

    fun removeGeofence(alarmId: String) {
        try {
            Log.d("GeofenceManager", "Removing geofence for alarm ID: $alarmId")
            geofencingClient.removeGeofences(listOf(alarmId))
                .addOnSuccessListener {
                    Log.d("GeofenceManager", "Geofence removed successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("GeofenceManager", "Failed to remove geofence", exception)
                }
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Exception removing geofence", e)
        }
    }

    fun removeAllGeofences() {
        try {
            Log.d("GeofenceManager", "Removing all geofences")
            geofencingClient.removeGeofences(geofencePendingIntent)
                .addOnSuccessListener {
                    Log.d("GeofenceManager", "All geofences removed successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("GeofenceManager", "Failed to remove all geofences", exception)
                }
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Exception removing all geofences", e)
        }
    }
}