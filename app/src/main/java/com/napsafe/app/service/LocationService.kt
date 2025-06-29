package com.napsafe.app.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.napsafe.app.data.model.LocationData
import com.napsafe.app.data.model.PlaceSearchResult
import com.napsafe.app.utils.GeographicUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationService @Inject constructor(
    private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Initialize Places API properly with error handling
    private val placesClient by lazy {
        try {
            if (!Places.isInitialized()) {
                Places.initialize(context, "AIzaSyANlPWqMCOOPdBkKPk2JM3F7JYVZ8hbOFs")
                Log.d("LocationService", "Places API initialized successfully")
            }
            Places.createClient(context)
        } catch (e: Exception) {
            Log.e("LocationService", "Failed to initialize Places API", e)
            throw e
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                continuation.resume(location)
            }
            .addOnFailureListener { exception ->
                Log.e("LocationService", "Failed to get current location", exception)
                continuation.resume(null)
            }
    }

    suspend fun searchPlaces(query: String): List<PlaceSearchResult> = suspendCancellableCoroutine { continuation ->
        try {
            Log.d("LocationService", "Searching for places with query: $query")

            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    Log.d("LocationService", "Found ${response.autocompletePredictions.size} predictions")
                    val results = response.autocompletePredictions.map { prediction ->
                        PlaceSearchResult(
                            placeId = prediction.placeId,
                            name = prediction.getPrimaryText(null).toString(),
                            address = prediction.getFullText(null).toString(),
                            latitude = 0.0, // Will be fetched when place is selected
                            longitude = 0.0
                        )
                    }
                    continuation.resume(results)
                }
                .addOnFailureListener { exception ->
                    Log.e("LocationService", "Places API search failed", exception)
                    continuation.resume(emptyList())
                }
        } catch (e: Exception) {
            Log.e("LocationService", "Places API search exception", e)
            continuation.resume(emptyList())
        }
    }

    suspend fun getPlaceDetails(placeId: String): LocationData? = suspendCancellableCoroutine { continuation ->
        try {
            Log.d("LocationService", "Fetching place details for ID: $placeId")

            val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val place = response.place
                    val latLng = place.latLng
                    if (latLng != null) {
                        Log.d("LocationService", "Place details fetched successfully: ${place.name}")
                        val locationData = LocationData(
                            latitude = latLng.latitude,
                            longitude = latLng.longitude,
                            address = place.address ?: "",
                            name = place.name ?: ""
                        )
                        continuation.resume(locationData)
                    } else {
                        Log.w("LocationService", "Place has no coordinates")
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("LocationService", "Place details fetch failed", exception)
                    continuation.resume(null)
                }
        } catch (e: Exception) {
            Log.e("LocationService", "Place details fetch exception", e)
            continuation.resume(null)
        }
    }

    /**
     * Calculate PRECISE distance between two points using Android's built-in method
     * This is the most accurate method for geographic calculations
     * Returns distance in meters with high precision
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val distance = GeographicUtils.calculatePreciseDistance(lat1, lon1, lat2, lon2)

        // Log for debugging radius accuracy
        Log.d("LocationService", "PRECISE Distance calculated: ${distance}m (${GeographicUtils.metersToKilometers(distance)}km)")

        return distance // Returns precise distance in meters
    }

    /**
     * Check if a location is within the specified radius of a target location
     * Uses PRECISE distance calculation with geographic accuracy
     */
    fun isWithinRadius(
        currentLat: Double,
        currentLon: Double,
        targetLat: Double,
        targetLon: Double,
        radiusInMeters: Float
    ): Boolean {
        val distance = calculateDistance(currentLat, currentLon, targetLat, targetLon)
        val isWithin = distance <= radiusInMeters

        Log.d("LocationService", "PRECISE Check - Distance: ${distance}m, Radius: ${radiusInMeters}m, Within: $isWithin")

        return isWithin
    }

    /**
     * Validate radius value for geographic accuracy
     */
    fun validateRadius(radiusInKm: Float): Boolean {
        return GeographicUtils.validateRadius(radiusInKm)
    }

    /**
     * Get formatted distance string for UI display
     */
    fun formatDistance(meters: Float): String {
        return GeographicUtils.formatDistance(meters)
    }
}