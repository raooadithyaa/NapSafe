package com.napsafe.app.ui.screens.map

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.napsafe.app.R
import com.napsafe.app.ui.components.BottomRadiusSlider
import com.napsafe.app.ui.components.LocationSearchBottomSheet
import com.napsafe.app.ui.components.MapLoadingScreen
import com.napsafe.app.ui.screens.settings.SettingsViewModel
import com.napsafe.app.ui.theme.Primary
import com.napsafe.app.utils.AlarmColorUtils
import kotlinx.coroutines.DisposableHandle
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.data.model.AlarmType
import android.media.RingtoneManager
import android.media.Ringtone
import android.media.AudioAttributes
import android.content.Context

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestNotificationPermissionIfNeeded(onDenied: () -> Unit = {}) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current
        var permissionRequested by remember { mutableStateOf(false) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (!granted) onDenied()
            }
        )
        LaunchedEffect(Unit) {
            if (!permissionRequested) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                permissionRequested = true
            }
        }
    }
}

@Composable
fun RequestLocationPermissionsIfNeeded(onPermissionsGranted: () -> Unit = {}) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val allGranted = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { results ->
            if (results.values.all { it }) {
                onPermissionsGranted()
            }
        }
    )
    LaunchedEffect(Unit) {
        if (!allGranted) {
            launcher.launch(permissions.toTypedArray())
        } else {
            onPermissionsGranted()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    var locationPermissionsGranted by remember { mutableStateOf(false) }
    RequestLocationPermissionsIfNeeded(onPermissionsGranted = { locationPermissionsGranted = true })
    RequestNotificationPermissionIfNeeded()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Get dark mode setting from SettingsViewModel
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val cameraPositionState = rememberCameraPositionState()

    // ✅ FIXED: Map properties that react to dark mode changes
    val mapProperties = remember(locationPermissions.allPermissionsGranted, settingsUiState.darkMode) {
        MapProperties(
            isMyLocationEnabled = locationPermissions.allPermissionsGranted,
            mapType = MapType.NORMAL,
            mapStyleOptions = null
        )
    }

    // Handle permissions efficiently
    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            viewModel.startLocationUpdates()
        }
    }

    // Auto-center on first location fix
    LaunchedEffect(uiState.shouldCenterOnUser, uiState.currentLocation) {
        if (uiState.shouldCenterOnUser && uiState.currentLocation != null) {
            val location = uiState.currentLocation!!
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    16f
                )
            )
            viewModel.onUserLocationCentered()
        }
    }

    // ✅ FIXED: SMART ZOOM - Only zoom if user needs to see the location better
    LaunchedEffect(uiState.shouldZoomToLocation) {
        uiState.shouldZoomToLocation?.let { location ->
            val currentZoom = cameraPositionState.position.zoom

            // Only zoom in if current zoom is too far out (less than 14)
            // This prevents jarring zoom changes when user is already zoomed in
            if (currentZoom < 14f) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude),
                        16f
                    )
                )
            } else {
                // If already zoomed in, just pan to the location without changing zoom
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLng(
                        LatLng(location.latitude, location.longitude)
                    )
                )
            }

            viewModel.onLocationZoomCompleted()
        }
    }

    // Pause location updates when leaving the Map screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLocationUpdates()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Only show loading when map is not ready
        if (!uiState.isMapReady) {
            MapLoadingScreen()
        }

        val mapUiSettings = remember {
            MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties, // ✅ FIXED: Reactive to dark mode changes
            uiSettings = mapUiSettings,
            onMapClick = { latLng ->
                // ✅ FIXED: Only add new markers on map tap, never remove existing ones
                viewModel.onMapTap(latLng)
            },
            onMapLoaded = {
                viewModel.onMapReady()
            }
        ) {
            // ✅ FIXED: Show marker only when location is tapped
            uiState.tappedLocation?.let { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Tap + to create alarm here",
                    snippet = "Selected location",
                    onClick = {
                        // ✅ FIXED: Remove marker ONLY when user clicks directly on THIS marker
                        viewModel.removeTappedLocation()
                        true // Consume the click to prevent map click
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
                    center = LatLng(location.latitude, location.longitude),
                    radius = preciseRadiusInMeters,
                    strokeColor = Primary,
                    fillColor = Primary.copy(alpha = 0.15f),
                    strokeWidth = 3f
                )
            }

            // Only render alarm circles when map is ready
            if (uiState.isMapReady) {
                uiState.activeAlarms.forEach { alarm ->
                    val strokeColor = AlarmColorUtils.getStrokeColorForAlarm(alarm.id, alarm.isActive)
                    val fillColor = AlarmColorUtils.getFillColorForAlarm(alarm.id, alarm.isActive)

                    Marker(
                        state = MarkerState(position = LatLng(alarm.latitude, alarm.longitude)),
                        title = alarm.name,
                        snippet = "${String.format("%.1f", alarm.radius / 1000f)}km radius"
                    )

                    Circle(
                        center = LatLng(alarm.latitude, alarm.longitude),
                        radius = alarm.radius.toDouble(),
                        strokeColor = strokeColor,
                        fillColor = fillColor,
                        strokeWidth = 2f
                    )
                }
            }
        }

        // Only show UI elements when map is ready
        if (uiState.isMapReady) {
            // 🎨 COMPACT: Smaller search bar (hide when setting up alarms)
            if (!uiState.showAlarmSetupBottomSheet) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp), // Reduced vertical padding
                    onClick = { viewModel.showSearchBottomSheet() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Reduced elevation
                    shape = RoundedCornerShape(14.dp) // Slightly smaller radius
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp), // Reduced padding
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp) // Smaller icon container
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
                                modifier = Modifier.size(18.dp) // Smaller icon
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Search for a location...",
                            style = MaterialTheme.typography.bodyMedium, // Smaller text
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // 🎨 PROFESSIONAL: Right-side button column with proper spacing
            if (!uiState.showAlarmSetupBottomSheet) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp), // Standard padding
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // My Location Button - Clean and professional
                    FloatingActionButton(
                        onClick = { viewModel.centerOnUserLocation() },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Primary,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(52.dp) // Slightly smaller
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // 🎨 PERFECT: Smaller Set Alarm button positioned lower
            if (uiState.tappedLocation != null && !uiState.showAlarmSetupBottomSheet) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp) // Moved lower - closer to bottom
                ) {
                    CompactSetAlarmButton(
                        onClick = { viewModel.createAlarmForTappedLocation() }
                    )
                }
            }

            // Bottom radius slider when creating alarm
            if (uiState.showAlarmSetupBottomSheet && uiState.selectedLocation != null) {
                BottomRadiusSlider(
                    selectedLocation = uiState.selectedLocation,
                    currentRadius = uiState.previewRadius,
                    onRadiusChange = viewModel::updatePreviewRadius,
                    onAlarmCreated = { alarm ->
                        if (locationPermissionsGranted) viewModel.createAlarm(alarm)
                    },
                    onDismiss = viewModel::clearSelectedLocation,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        // Permission request UI
        if (!locationPermissions.allPermissionsGranted && uiState.isMapReady) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Location Access Required",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This app needs location access to set location-based alarms and show your position on the map.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { locationPermissions.launchMultiplePermissionRequest() },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Grant Permission",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
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

// 🎨 PERFECT: Compact, well-positioned Set Alarm Button
@Composable
private fun CompactSetAlarmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Subtle animation for professional feel
    val infiniteTransition = rememberInfiniteTransition(label = "alarm_button_animation")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Subtle outer glow - smaller
        Box(
            modifier = Modifier
                .size(100.dp) // Reduced from 140dp
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF10B981).copy(alpha = glowAlpha * 0.15f),
                            Color(0xFF10B981).copy(alpha = glowAlpha * 0.08f),
                            Color.Transparent
                        ),
                        radius = 50.dp.value
                    ),
                    shape = CircleShape
                )
        )

        // Main button - SMALLER and positioned lower
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(130.dp) // Reduced from 160dp
                .height(48.dp) // Reduced from 56dp
                .shadow(
                    elevation = 6.dp, // Reduced shadow
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color(0xFF10B981).copy(alpha = 0.2f),
                    spotColor = Color(0xFF10B981).copy(alpha = 0.2f)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF10B981), // Emerald green
                                Color(0xFF059669), // Darker emerald
                                Color(0xFF047857)  // Deep emerald
                            ),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(150f, 80f)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Beautiful alarm icon - smaller
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Set Alarm",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp) // Reduced from 20dp
                    )

                    Spacer(modifier = Modifier.width(6.dp)) // Reduced spacing

                    // Clear, readable text - smaller
                    Text(
                        text = "SET ALARM",
                        color = Color.White,
                        fontSize = 12.sp, // Reduced from 14sp
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}

fun playTestAlarmSound(context: Context) {
    val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    val ringtone = RingtoneManager.getRingtone(context, alarmUri)
    if (ringtone != null) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            ringtone.audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        }
        ringtone.play()
    }
}