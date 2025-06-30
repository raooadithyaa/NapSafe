package com.napsafe.app.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.data.model.LocationData
import com.napsafe.app.data.model.PlaceSearchResult
import com.napsafe.app.data.repository.LocationAlarmRepository
import com.napsafe.app.service.GeofenceManager
import com.napsafe.app.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val currentLocation: Location? = null,
    val tappedLocation: LocationData? = null,
    val selectedLocation: LocationData? = null,
    val activeAlarms: List<LocationAlarm> = emptyList(),
    val searchResults: List<PlaceSearchResult> = emptyList(),
    val previewRadius: Float = 0.0f,
    val isLoading: Boolean = false,
    val isMapReady: Boolean = false,
    val error: String? = null,
    val showSearchBottomSheet: Boolean = false,
    val showAlarmSetupBottomSheet: Boolean = false,
    val shouldCenterOnUser: Boolean = false,
    val shouldZoomToLocation: LocationData? = null, // ✅ NEW: Smart zoom control
    val isLocationPermissionGranted: Boolean = false,
    val hasInitialLocationFix: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: LocationAlarmRepository,
    private val locationService: LocationService,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    companion object {
        private const val FIRST_FIX_UPDATE_INTERVAL = 5000L
        private const val FIRST_FIX_MIN_UPDATE_INTERVAL = 2000L
        private const val NORMAL_UPDATE_INTERVAL = 15000L
        private const val NORMAL_MIN_UPDATE_INTERVAL = 10000L
        private const val MAX_UPDATE_DELAY = 30000L
        private const val OPTIMIZED_UPDATE_INTERVAL = 20000L
        private const val OPTIMIZED_MIN_UPDATE_INTERVAL = 15000L
        private const val OPTIMIZED_MAX_UPDATE_DELAY = 60000L
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    // Debounced search to prevent excessive API calls
    private val searchQuery = MutableStateFlow("")

    init {
        observeActiveAlarms()
        setupDebouncedSearch()
    }

    // Efficient alarm observation with minimal recomposition
    private fun observeActiveAlarms() {
        viewModelScope.launch {
            repository.getActiveAlarms()
                .distinctUntilChanged()
                .collect { alarms ->
                    _uiState.update { it.copy(activeAlarms = alarms) }
                    com.napsafe.app.service.LocationTrackingService.setActiveAlarms(alarms)
                }
        }
    }

    // Debounced search to prevent API spam
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun setupDebouncedSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    flow {
                        try {
                            _uiState.update { it.copy(isLoading = true) }
                            val results = locationService.searchPlaces(query)
                            emit(results)
                        } catch (e: Exception) {
                            emit(emptyList<PlaceSearchResult>())
                            _uiState.update { it.copy(error = e.message) }
                        } finally {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
                .collect { results ->
                    _uiState.update { it.copy(searchResults = results) }
                }
        }
    }

    // Optimized location updates with smart intervals
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            if (_uiState.value.hasInitialLocationFix) NORMAL_UPDATE_INTERVAL else FIRST_FIX_UPDATE_INTERVAL
        ).apply {
            setMinUpdateIntervalMillis(if (_uiState.value.hasInitialLocationFix) NORMAL_MIN_UPDATE_INTERVAL else FIRST_FIX_MIN_UPDATE_INTERVAL)
            setMaxUpdateDelayMillis(MAX_UPDATE_DELAY)
            setWaitForAccurateLocation(false)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val isFirstFix = !_uiState.value.hasInitialLocationFix

                    _uiState.update { state ->
                        state.copy(
                            currentLocation = location,
                            hasInitialLocationFix = true,
                            shouldCenterOnUser = isFirstFix,
                            isLocationPermissionGranted = true
                        )
                    }

                    if (_uiState.value.activeAlarms.isNotEmpty()) {
                        checkProximityToAlarms(location)
                    }

                    if (isFirstFix) {
                        updateLocationRequestInterval()
                    }
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (!availability.isLocationAvailable) {
                    _uiState.update { it.copy(error = "Location services unavailable") }
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
                        shouldCenterOnUser = !state.hasInitialLocationFix,
                        hasInitialLocationFix = true,
                        isLocationPermissionGranted = true
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationRequestInterval() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)

            val optimizedRequest = LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                OPTIMIZED_UPDATE_INTERVAL
            ).apply {
                setMinUpdateIntervalMillis(OPTIMIZED_MIN_UPDATE_INTERVAL)
                setMaxUpdateDelayMillis(OPTIMIZED_MAX_UPDATE_DELAY)
            }.build()

            fusedLocationClient.requestLocationUpdates(optimizedRequest, callback, null)
        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    fun onMapReady() {
        _uiState.update { it.copy(isMapReady = true) }
    }

    // ✅ FIXED: PRECISE marker behavior - only add new markers, never remove on map tap
    fun onMapTap(latLng: LatLng) {
        // Always add a new marker at the tapped location
        // Marker removal is ONLY handled by direct marker click
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

    // ✅ FIXED: Function to remove tapped location marker - ONLY called by direct marker click
    fun removeTappedLocation() {
        _uiState.update {
            it.copy(
                tappedLocation = null,
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
                    previewRadius = 0.0f
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

    // ✅ NEW: Smart zoom completion handler
    fun onLocationZoomCompleted() {
        _uiState.update { it.copy(shouldZoomToLocation = null) }
    }

    fun searchPlaces(query: String) {
        searchQuery.value = query
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    // ✅ FIXED: Smart zoom for search results - only zoom if needed
    fun selectPlace(place: PlaceSearchResult) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val locationData = locationService.getPlaceDetails(place.placeId)
                locationData?.let { location ->
                    _uiState.update {
                        it.copy(
                            selectedLocation = location,
                            showSearchBottomSheet = false,
                            showAlarmSetupBottomSheet = true,
                            searchResults = emptyList(),
                            previewRadius = 0.0f,
                            tappedLocation = null,
                            shouldZoomToLocation = location, // ✅ SMART ZOOM: Only if needed
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
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
                    previewRadius = 0.0f,
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
                previewRadius = 0.0f,
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
        searchQuery.value = ""
    }

    fun createAlarm(alarm: LocationAlarm) {
        viewModelScope.launch {
            try {
                repository.insertAlarm(alarm)
                if (alarm.isActive) {
                    geofenceManager.addGeofence(alarm)
                    // Start LocationTrackingService for persistent notification
                    val intent = Intent(context, com.napsafe.app.service.LocationTrackingService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    } else {
                        context.startService(intent)
                    }
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
        val activeAlarms = _uiState.value.activeAlarms
        if (activeAlarms.isEmpty()) return

        activeAlarms.forEach { alarm ->
            if (!alarm.isActive) return@forEach

            val distance = locationService.calculateDistance(
                currentLocation.latitude,
                currentLocation.longitude,
                alarm.latitude,
                alarm.longitude
            )

            if (distance <= alarm.radius) {
                triggerAlarm(alarm)
            }
        }
    }

    private fun triggerAlarm(alarm: LocationAlarm) {
        viewModelScope.launch {
            repository.updateAlarmStatus(alarm.id, false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}