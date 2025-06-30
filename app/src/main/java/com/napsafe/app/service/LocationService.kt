package com.napsafe.app.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.napsafe.app.data.model.LocationData
import com.napsafe.app.data.model.PlaceSearchResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class LocationService @Inject constructor(
    private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val placesClient: PlacesClient by lazy { Places.createClient(context) }
    private val geocoder: Geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    // PERFORMANCE: Cache session token to reduce API costs
    private var sessionToken: AutocompleteSessionToken? = null

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                continuation.resume(location)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    // PERFORMANCE: Optimized search with timeout and caching
    suspend fun searchPlaces(query: String): List<PlaceSearchResult> {
        if (query.isBlank()) return emptyList()

        return withTimeoutOrNull(5000) { // PERFORMANCE: 5-second timeout
            suspendCancellableCoroutine { continuation ->
                // PERFORMANCE: Reuse session token to reduce costs
                if (sessionToken == null) {
                    sessionToken = AutocompleteSessionToken.newInstance()
                }

                val request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(query)
                    .setSessionToken(sessionToken)
                    .build()

                placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener { response ->
                        val results = response.autocompletePredictions.take(5).map { prediction -> // PERFORMANCE: Limit to 5 results
                            PlaceSearchResult(
                                placeId = prediction.placeId,
                                name = prediction.getPrimaryText(null).toString(),
                                address = prediction.getSecondaryText(null).toString(),
                                latitude = 0.0, // Will be fetched when selected
                                longitude = 0.0
                            )
                        }
                        continuation.resume(results)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        } ?: emptyList()
    }

    // PERFORMANCE: Optimized place details fetching
    suspend fun getPlaceDetails(placeId: String): LocationData? {
        return withTimeoutOrNull(5000) { // PERFORMANCE: 5-second timeout
            suspendCancellableCoroutine { continuation ->
                val placeFields = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
                )

                val request = FetchPlaceRequest.newInstance(placeId, placeFields)

                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        val place = response.place
                        val latLng = place.latLng

                        if (latLng != null) {
                            val locationData = LocationData(
                                latitude = latLng.latitude,
                                longitude = latLng.longitude,
                                address = place.address ?: "Unknown address",
                                name = place.name ?: "Unknown place"
                            )
                            continuation.resume(locationData)
                        } else {
                            continuation.resume(null)
                        }

                        // PERFORMANCE: Reset session token after use
                        sessionToken = null
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                        sessionToken = null // Reset on error too
                    }
            }
        }
    }

    // PERFORMANCE: Optimized distance calculation using Android's built-in method
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    // PERFORMANCE: Efficient reverse geocoding with caching potential
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        return withTimeoutOrNull(3000) { // PERFORMANCE: 3-second timeout for geocoding
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
            } catch (e: Exception) {
                "Unknown location"
            }
        } ?: "Unknown location"
    }
}