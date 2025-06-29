package com.napsafe.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "location_alarms")
data class LocationAlarm(
    @PrimaryKey
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val name: String,
    val radius: Float, // in meters
    val isActive: Boolean,
    val alarmType: AlarmType,
    val volume: Float, // 0.0 to 1.0
    val createdAt: Long,
    val geofenceId: String? = null
) : Parcelable

@Parcelize
enum class AlarmType : Parcelable {
    NOTIFICATION,
    SOUND,
    VIBRATION
}

@Parcelize
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val name: String
) : Parcelable

@Parcelize
data class PlaceSearchResult(
    val placeId: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable