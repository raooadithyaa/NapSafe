package com.napsafe.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.napsafe.app.data.model.AlarmType
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.data.model.LocationData
import com.napsafe.app.ui.theme.Primary
import com.napsafe.app.utils.GeographicUtils
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomRadiusSlider(
    selectedLocation: LocationData?,
    currentRadius: Float,
    onRadiusChange: (Float) -> Unit,
    onAlarmCreated: (LocationAlarm) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var alarmName by remember { mutableStateOf("") }
    var showNameDialog by remember { mutableStateOf(false) }

    // Smooth animation for radius value
    val animatedRadius by animateFloatAsState(
        targetValue = currentRadius,
        animationSpec = tween(durationMillis = 100, easing = EaseOutCubic),
        label = "radius_animation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 12.dp) // REDUCED padding for less screen height
        ) {
            // Top indicator bar
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp)) // REDUCED spacing

            // Header row with radius display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Radius display
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (animatedRadius == 0.0f) "0" else String.format("%.1f", animatedRadius),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (animatedRadius == 0.0f)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else Primary,
                        fontSize = 28.sp // REDUCED font size
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "km",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 16.sp // REDUCED font size
                    )

                    // Show precise meters for small values
                    if (animatedRadius > 0.0f && animatedRadius < 1.0f) {
                        Spacer(modifier = Modifier.width(6.dp))
                        val preciseMeters = GeographicUtils.kilometersToMeters(animatedRadius).toInt()
                        Text(
                            text = "(${preciseMeters}m)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                }

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp) // REDUCED spacing
                ) {
                    // Name button
                    OutlinedButton(
                        onClick = { showNameDialog = true },
                        modifier = Modifier.height(36.dp), // REDUCED height
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            "Name",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Create alarm button
                    Button(
                        onClick = {
                            if (currentRadius > 0.0f) {
                                selectedLocation?.let { location ->
                                    val finalName = if (alarmName.isBlank()) {
                                        when {
                                            location.name.isNotBlank() && location.name != "My Location" -> location.name
                                            location.address.contains("Current") -> "My Location Alarm"
                                            else -> "Location Alarm"
                                        }
                                    } else {
                                        alarmName
                                    }

                                    val preciseRadiusInMeters = GeographicUtils.kilometersToMeters(currentRadius)

                                    val alarm = LocationAlarm(
                                        id = UUID.randomUUID().toString(),
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        address = location.address,
                                        name = finalName,
                                        radius = preciseRadiusInMeters,
                                        isActive = true,
                                        alarmType = AlarmType.NOTIFICATION,
                                        volume = 0.8f,
                                        createdAt = System.currentTimeMillis()
                                    )
                                    onAlarmCreated(alarm)
                                }
                            }
                        },
                        enabled = currentRadius > 0.0f && GeographicUtils.validateRadius(currentRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(36.dp) // REDUCED height
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (currentRadius > 0.0f) "Create" else "Set Radius",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // REDUCED spacing

            // ✅ CLEAN, INTUITIVE SLIDER - NO CONFUSION!
            Column {
                // SINGLE CLEAN SLIDER with proper visual feedback
                Slider(
                    value = currentRadius,
                    onValueChange = { newRadius ->
                        if (GeographicUtils.validateRadius(newRadius)) {
                            onRadiusChange(newRadius)
                        }
                    },
                    valueRange = 0.0f..20f,
                    steps = 200, // 0.1km increments
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    track = { sliderState ->
                        // CLEAN track with proper left-to-right visual feedback
                        SliderDefaults.Track(
                            sliderState = sliderState,
                            modifier = Modifier.height(6.dp),
                            colors = SliderDefaults.colors(
                                activeTrackColor = Primary, // LEFT side (filled) = Primary blue
                                inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f) // RIGHT side (empty) = Light gray
                            ),
                            thumbTrackGapSize = 0.dp,
                            trackInsideCornerSize = 0.dp
                        )
                    },
                    thumb = {
                        // Beautiful custom thumb
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .shadow(4.dp, CircleShape)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(Primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color.White, CircleShape)
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp)) // REDUCED spacing

                // Range labels and quick presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "0km",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 10.sp
                    )

                    // UPDATED quick preset chips: 500m, 1km, 2km, 3km, 5km
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp) // REDUCED spacing
                    ) {
                        val presets = listOf(0.5f, 1.0f, 2.0f, 3.0f, 5.0f)
                        presets.forEach { preset ->
                            val isSelected = kotlin.math.abs(currentRadius - preset) < 0.05f

                            FilterChip(
                                onClick = { onRadiusChange(preset) },
                                label = {
                                    Text(
                                        text = if (preset < 1f) "${(preset * 1000).toInt()}m" else "${preset.toInt()}km",
                                        fontSize = 9.sp, // REDUCED font size
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                    )
                                },
                                selected = isSelected,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Primary.copy(alpha = 0.2f),
                                    selectedLabelColor = Primary,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.height(24.dp) // REDUCED height
                            )
                        }
                    }

                    Text(
                        text = "20km",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }

    // Name input dialog
    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = {
                Text(
                    "Alarm Name",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "Give your alarm a custom name (optional)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = alarmName,
                        onValueChange = { alarmName = it },
                        placeholder = { Text("Home, Work, etc.") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            focusedLabelColor = Primary
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNameDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    alarmName = ""
                    showNameDialog = false
                }) {
                    Text("Clear")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}