package com.napsafe.app.utils

import android.location.Location
import kotlin.math.*

/**
 * Geographic utility functions for precise distance and radius calculations
 */
object GeographicUtils {

    // Earth's radius in meters (WGS84 ellipsoid)
    private const val EARTH_RADIUS_METERS = 6378137.0

    /**
     * Calculate precise distance between two geographic points using Haversine formula
     * Returns distance in meters with high precision
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_METERS * c
    }

    /**
     * Calculate precise distance using Android's Location.distanceBetween
     * This is the most accurate method for geographic calculations
     */
    fun calculatePreciseDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] // Returns distance in meters
    }

    /**
     * Check if a point is within a circular radius of a target location
     * Uses precise geographic distance calculation
     */
    fun isWithinRadius(
        currentLat: Double,
        currentLon: Double,
        targetLat: Double,
        targetLon: Double,
        radiusInMeters: Float
    ): Boolean {
        val distance = calculatePreciseDistance(currentLat, currentLon, targetLat, targetLon)
        return distance <= radiusInMeters
    }

    /**
     * Convert kilometers to meters with precision
     */
    fun kilometersToMeters(kilometers: Float): Float {
        return kilometers * 1000.0f
    }

    /**
     * Convert meters to kilometers with precision
     */
    fun metersToKilometers(meters: Float): Float {
        return meters / 1000.0f
    }

    /**
     * Validate that radius is within reasonable bounds for location alarms
     */
    fun validateRadius(radiusInKm: Float): Boolean {
        return radiusInKm >= 0.0f && radiusInKm <= 100.0f // Max 100km radius
    }

    /**
     * Get a human-readable distance string
     */
    fun formatDistance(meters: Float): String {
        return when {
            meters < 1000 -> "${meters.toInt()}m"
            meters < 10000 -> "${String.format("%.1f", meters / 1000f)}km"
            else -> "${(meters / 1000f).toInt()}km"
        }
    }

    /**
     * Calculate the approximate zoom level needed to show a radius on the map
     * This helps with automatic map zoom when creating alarms
     */
    fun calculateZoomForRadius(radiusInMeters: Float): Float {
        return when {
            radiusInMeters <= 100 -> 18f      // Very close zoom for small radius
            radiusInMeters <= 500 -> 16f      // Close zoom
            radiusInMeters <= 1000 -> 15f     // Medium zoom
            radiusInMeters <= 2000 -> 14f     // Medium-far zoom
            radiusInMeters <= 5000 -> 13f     // Far zoom
            radiusInMeters <= 10000 -> 12f    // Very far zoom
            else -> 11f                       // Maximum zoom out
        }
    }
}