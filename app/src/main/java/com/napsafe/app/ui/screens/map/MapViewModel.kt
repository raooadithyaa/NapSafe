package com.napsafe.app.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.data.model.LocationData
import com.napsafe.app.data.model.PlaceSearchResult
import com.napsafe.app.data.repository.LocationAlarmRepository
import com.napsafe.app.service.GeofenceManager
import com.napsafe.app.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val currentLocation: Location? = null,
    val tappedLocation: LocationData? = null,
    val selectedLocation: LocationData? = null,
    val activeAlarms: List<LocationAlarm> = emptyList(),
    val searchResults: List<PlaceSearchResult> = emptyList(),
    val previewRadius: Float = 0.0f, // FIXED: Start at 0km - no circle initially
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSearchBottomSheet: Boolean = false,
    val showAlarmSetupBottomSheet: Boolean = false,
    val shouldCenterOnUser: Boolean = false
)

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: LocationAlarmRepository,
    private val locationService: LocationService,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    init {
        observeActiveAlarms()
    }

    private fun observeActiveAlarms() {
        viewModelScope.launch {
            repository.getActiveAlarms().collect { alarms ->
                _uiState.update { it.copy(activeAlarms = alarms) }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L
        ).setMinUpdateIntervalMillis(5000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    _uiState.update { it.copy(currentLocation = location) }
                    checkProximityToAlarms(location)
                }
            }
        }

        locationCallback?.let { callback ->
            fusedLocationClient.requestLocationUpdates(locationRequest, callback, null)
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                _uiState.update { state ->
                    state.copy(
                        currentLocation = it,
                        shouldCenterOnUser = true
                    )
                }
            }
        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    fun onMapTap(latLng: LatLng) {
        val locationData = LocationData(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            address = "Selected Location",
            name = "Map Location"
        )

        _uiState.update {
            it.copy(
                tappedLocation = locationData,
                selectedLocation = null,
                showAlarmSetupBottomSheet = false
            )
        }
    }

    fun createAlarmForTappedLocation() {
        _uiState.value.tappedLocation?.let { location ->
            _uiState.update {
                it.copy(
                    selectedLocation = location,
                    showAlarmSetupBottomSheet = true,
                    previewRadius = 0.0f // FIXED: Start at 0km when creating alarm
                )
            }
        }
    }

    fun updatePreviewRadius(radius: Float) {
        _uiState.update { it.copy(previewRadius = radius) }
    }

    fun centerOnUserLocation() {
        _uiState.update { it.copy(shouldCenterOnUser = true) }
    }

    fun onUserLocationCentered() {
        _uiState.update { it.copy(shouldCenterOnUser = false) }
    }

    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val results = locationService.searchPlaces(query)
                _uiState.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message,
                        isLoading = false,
                        searchResults = emptyList()
                    )
                }
            }
        }
    }

    fun selectPlace(place: PlaceSearchResult) {
        viewModelScope.launch {
            try {
                val locationData = locationService.getPlaceDetails(place.placeId)
                locationData?.let { location ->
                    _uiState.update {
                        it.copy(
                            selectedLocation = location,
                            showSearchBottomSheet = false,
                            showAlarmSetupBottomSheet = true,
                            searchResults = emptyList(),
                            previewRadius = 0.0f, // FIXED: Start at 0km for search results too
                            tappedLocation = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun selectCurrentLocation() {
        _uiState.value.currentLocation?.let { location ->
            val locationData = LocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                address = "Current Location",
                name = "My Location"
            )

            _uiState.update {
                it.copy(
                    selectedLocation = locationData,
                    showAlarmSetupBottomSheet = true,
                    previewRadius = 0.0f, // FIXED: Start at 0km for current location too
                    tappedLocation = null
                )
            }
        }
    }

    fun clearSelectedLocation() {
        _uiState.update {
            it.copy(
                selectedLocation = null,
                showAlarmSetupBottomSheet = false,
                previewRadius = 0.0f, // FIXED: Reset to 0km
                tappedLocation = null
            )
        }
    }

    fun showSearchBottomSheet() {
        _uiState.update { it.copy(showSearchBottomSheet = true) }
    }

    fun hideSearchBottomSheet() {
        _uiState.update {
            it.copy(
                showSearchBottomSheet = false,
                searchResults = emptyList()
            )
        }
    }

    fun createAlarm(alarm: LocationAlarm) {
        viewModelScope.launch {
            try {
                repository.insertAlarm(alarm)
                if (alarm.isActive) {
                    geofenceManager.addGeofence(alarm)
                }
                clearSelectedLocation()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun simulateLocationForTesting(targetAlarm: LocationAlarm) {
        viewModelScope.launch {
            try {
                val testLocation = Location("test").apply {
                    latitude = targetAlarm.latitude
                    longitude = targetAlarm.longitude
                    accuracy = 10f
                }

                _uiState.update { it.copy(currentLocation = testLocation) }
                checkProximityToAlarms(testLocation)

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Testing failed: ${e.message}") }
            }
        }
    }

    private fun checkProximityToAlarms(currentLocation: Location) {
        _uiState.value.activeAlarms.forEach { alarm ->
            val distance = locationService.calculateDistance(
                currentLocation.latitude,
                currentLocation.longitude,
                alarm.latitude,
                alarm.longitude
            )

            if (distance <= alarm.radius && alarm.isActive) {
                triggerAlarm(alarm)
            }
        }
    }

    private fun triggerAlarm(alarm: LocationAlarm) {
        viewModelScope.launch {
            repository.updateAlarmStatus(alarm.id, false)
        }
    }

    fun clearTappedLocation() {
        _uiState.update { it.copy(tappedLocation = null) }
    }

    fun updateTappedLocation(latLng: com.google.android.gms.maps.model.LatLng) {
        val locationData = com.napsafe.app.data.model.LocationData(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            address = "Selected Location",
            name = "Map Location"
        )
        _uiState.update { it.copy(tappedLocation = locationData) }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}