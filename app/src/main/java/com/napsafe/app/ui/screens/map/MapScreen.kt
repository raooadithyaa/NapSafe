package com.napsafe.app.ui.screens.map

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.napsafe.app.R
import com.napsafe.app.ui.components.BottomRadiusSlider
import com.napsafe.app.ui.components.LocationSearchBottomSheet
import com.napsafe.app.ui.components.MapLoadingScreen
import com.napsafe.app.ui.screens.settings.SettingsViewModel
import com.napsafe.app.ui.theme.Primary
import com.napsafe.app.utils.AlarmColorUtils
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Get dark mode setting from shared SettingsViewModel
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            com.google.android.gms.maps.model.LatLng(0.0, 0.0), // Start at world view
            2f // Low zoom for faster initial load
        )
    }
    var isMapLoaded by rememberSaveable { mutableStateOf(false) }
    
    // Check if system is in dark mode - OPTIMIZED
    val isSystemDarkMode = isSystemInDarkTheme()
    
    // Use system dark mode OR custom dark mode setting - OPTIMIZED
    val shouldUseDarkMap = isSystemDarkMode || settingsUiState.darkMode

    // OPTIMIZED: Remove debug logging and unnecessary mapStyleVersion
    LaunchedEffect(shouldUseDarkMap) {
        // Force map style refresh when dark mode changes
        // No need for mapStyleVersion tracking
    }

    // OPTIMIZED: Cache map style to avoid repeated file I/O
    val mapStyleOptions = remember(shouldUseDarkMap) {
        if (shouldUseDarkMap) {
            try {
                MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
            } catch (e: Exception) {
                null
            }
        } else {
            null // Use default light style
        }
    }

    LaunchedEffect(Unit) {
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        } else {
            viewModel.startLocationUpdates()
        }
    }

    LaunchedEffect(uiState.shouldCenterOnUser, isMapLoaded) {
        if (isMapLoaded && uiState.shouldCenterOnUser && uiState.currentLocation != null) {
            val location = uiState.currentLocation!!
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude),
                    15f
                )
            )
            viewModel.onUserLocationCentered()
        }
    }

    LaunchedEffect(uiState.tappedLocation, isMapLoaded) {
        if (isMapLoaded && uiState.tappedLocation != null) {
            val location = uiState.tappedLocation
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    com.google.android.gms.maps.model.LatLng(location!!.latitude, location.longitude),
                    16f
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // FORCED PERMISSION REQUEST - Block everything until permission granted
        if (!locationPermissions.allPermissionsGranted) {
            PermissionRequestScreen(
                onGrantPermission = { locationPermissions.launchMultiplePermissionRequest() }
            )
        } else {
            // Only show map content when permission is granted and map is loaded
            // DEBUG: Always show the map for now, regardless of isMapLoaded
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true, // Always enabled when permission granted
                    mapType = MapType.NORMAL,
                    mapStyleOptions = mapStyleOptions, // OPTIMIZED dark mode style
                    isIndoorEnabled = false, // Disable indoor maps for faster loading
                    isTrafficEnabled = false, // Disable traffic for faster loading
                    minZoomPreference = 10f, // Set minimum zoom for better performance
                    maxZoomPreference = 20f // Set maximum zoom
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = false, // We have custom button
                    zoomControlsEnabled = false, // We have custom controls
                    compassEnabled = false, // Disable for cleaner UI
                    mapToolbarEnabled = false, // Disable for cleaner UI
                    rotationGesturesEnabled = false, // Disable rotation
                    tiltGesturesEnabled = false, // Disable tilt
                    scrollGesturesEnabled = true,
                    zoomGesturesEnabled = true
                ),
                onMapClick = { latLng ->
                    viewModel.onMapTap(latLng)
                },
                onMapLoaded = {
                    println("Map loaded!")
                    isMapLoaded = true
                }
            ) {
                // Map overlays - must be inside GoogleMap lambda for proper context
                if (isMapLoaded) {
                    // Red marker when location is tapped
                    uiState.tappedLocation?.let { location ->
                        val tappedMarkerState = remember {
                            MarkerState(position = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude))
                        }
                        // Keep marker in sync with state
                        LaunchedEffect(location) {
                            tappedMarkerState.position = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)
                        }
                        // Workaround: update ViewModel when marker is dragged
                        LaunchedEffect(tappedMarkerState.position) {
                            if (tappedMarkerState.position.latitude != location.latitude ||
                                tappedMarkerState.position.longitude != location.longitude) {
                                viewModel.updateTappedLocation(tappedMarkerState.position)
                            }
                        }
                        Marker(
                            state = tappedMarkerState,
                            draggable = true,
                            onClick = {
                                viewModel.clearTappedLocation()
                                true
                            }
                        )
                    }

                    // Preview circle when setting up alarm (only if radius > 0)
                    if (uiState.showAlarmSetupBottomSheet &&
                        uiState.selectedLocation != null &&
                        uiState.previewRadius > 0.0f) {

                        val location = uiState.selectedLocation!!
                        val preciseRadiusInMeters = (uiState.previewRadius * 1000.0).toDouble()

                        Circle(
                            center = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude),
                            radius = preciseRadiusInMeters,
                            strokeColor = Primary,
                            fillColor = Primary.copy(alpha = 0.15f),
                            strokeWidth = 3f
                        )
                    }

                    // Created alarms with UNIQUE COLORS for each alarm
                    // OPTIMIZED: Memoize color calculations to avoid repeated computation
                    val alarmColors = remember(uiState.activeAlarms) {
                        uiState.activeAlarms.associate { alarm ->
                            alarm.id to Pair(
                                AlarmColorUtils.getStrokeColorForAlarm(alarm.id, alarm.isActive),
                                AlarmColorUtils.getFillColorForAlarm(alarm.id, alarm.isActive)
                            )
                        }
                    }
                    
                    uiState.activeAlarms.forEach { alarm ->
                        val (strokeColor, fillColor) = alarmColors[alarm.id] ?: Pair(Primary, Primary.copy(alpha = 0.3f))

                        Marker(
                            state = MarkerState(position = com.google.android.gms.maps.model.LatLng(alarm.latitude, alarm.longitude)),
                            title = alarm.name,
                            snippet = "${String.format("%.1f", alarm.radius / 1000f)}km radius"
                        )

                        Circle(
                            center = com.google.android.gms.maps.model.LatLng(alarm.latitude, alarm.longitude),
                            radius = alarm.radius.toDouble(),
                            strokeColor = strokeColor,
                            fillColor = fillColor,
                            strokeWidth = 2f
                        )
                    }
                }
            }

            // Optionally, show loading screen if not loaded
            if (!isMapLoaded) {
                MapLoadingScreen()
            }

            // Top search bar (hide when setting up alarms)
            if (!uiState.showAlarmSetupBottomSheet && isMapLoaded) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { viewModel.showSearchBottomSheet() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Primary.copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "Search for a location...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Floating action buttons (hide when setting up alarms)
            if (!uiState.showAlarmSetupBottomSheet && isMapLoaded) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FloatingActionButton(
                        onClick = { viewModel.centerOnUserLocation() },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Primary,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Modern Extended FAB: Only show when marker is present and not setting up alarm
                AnimatedVisibility(
                    visible = uiState.tappedLocation != null && !uiState.showAlarmSetupBottomSheet && isMapLoaded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ExtendedFloatingActionButton(
                        text = { Text("Set Alarm", color = Color.White) },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_location),
                                contentDescription = "Set Alarm",
                                tint = Color.White
                            )
                        },
                        containerColor = Primary,
                        contentColor = Color.White,
                        onClick = {
                            viewModel.createAlarmForTappedLocation()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp, bottom = 32.dp)
                            .shadow(12.dp, shape = RoundedCornerShape(32.dp))
                    )
                }
            }

            // Bottom radius slider when creating alarm
            if (uiState.showAlarmSetupBottomSheet && uiState.selectedLocation != null && isMapLoaded) {
                BottomRadiusSlider(
                    selectedLocation = uiState.selectedLocation,
                    currentRadius = uiState.previewRadius,
                    onRadiusChange = viewModel::updatePreviewRadius,
                    onAlarmCreated = { alarm ->
                        viewModel.createAlarm(alarm)
                    },
                    onDismiss = viewModel::clearSelectedLocation,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

    // Search bottom sheet
    if (uiState.showSearchBottomSheet) {
        LocationSearchBottomSheet(
            searchResults = uiState.searchResults,
            onSearch = viewModel::searchPlaces,
            onLocationSelected = viewModel::selectPlace,
            onDismiss = viewModel::hideSearchBottomSheet
        )
    }
}

@Composable
private fun PermissionRequestScreen(
    onGrantPermission: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Location icon with animation
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Primary.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Location Access Required",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This app needs location access to create location-based alarms and show your position on the map. Without this permission, the app cannot function.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onGrantPermission,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Grant Location Permission",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You can change this later in your device settings",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}