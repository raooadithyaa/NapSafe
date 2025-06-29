package com.napsafe.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.napsafe.app.data.model.AlarmType
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.data.model.LocationData
import com.napsafe.app.ui.theme.Primary
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiLocationBottomSheet(
    selectedLocations: List<LocationData>,
    currentSelectedIndex: Int,
    currentRadius: Float,
    onRadiusChange: (Float) -> Unit,
    onLocationSelected: (Int) -> Unit,
    onLocationRemoved: (Int) -> Unit,
    onAlarmCreated: (LocationAlarm) -> Unit,
    onDismiss: () -> Unit,
    getLocationColor: (Int) -> Color,
    modifier: Modifier = Modifier
) {
    var alarmName by remember { mutableStateOf("") }
    var showNameDialog by remember { mutableStateOf(false) }

    val animatedRadius by animateFloatAsState(
        targetValue = currentRadius,
        animationSpec = tween(durationMillis = 150, easing = EaseOutCubic),
        label = "radius_animation"
    )

    val currentLocation = selectedLocations.getOrNull(currentSelectedIndex)
    val currentColor = getLocationColor(currentSelectedIndex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 12.dp)
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

            Spacer(modifier = Modifier.height(12.dp))

            // Location selector row
            if (selectedLocations.size > 1) {
                Text(
                    text = "Select Location (${selectedLocations.size} selected)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(selectedLocations) { index, location ->
                        val isSelected = index == currentSelectedIndex
                        val color = getLocationColor(index)

                        Card(
                            modifier = Modifier
                                .clickable { onLocationSelected(index) }
                                .height(40.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(color, CircleShape)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = location.name.take(15),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )

                                if (selectedLocations.size > 1) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { onLocationRemoved(index) },
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Remove",
                                            modifier = Modifier.size(12.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Current location configuration
            currentLocation?.let { location ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%.1f", animatedRadius),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = currentColor,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "km",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 16.sp
                        )

                        if (animatedRadius < 1.0f) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${(animatedRadius * 1000).toInt()}m)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showNameDialog = true },
                            modifier = Modifier.height(40.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = currentColor
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Name",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = {
                                val finalName = if (alarmName.isBlank()) {
                                    when {
                                        location.name.isNotBlank() && location.name != "My Location" -> location.name
                                        location.address.contains("Current") -> "My Location Alarm"
                                        else -> "Location Alarm"
                                    }
                                } else {
                                    alarmName
                                }

                                val preciseRadiusInMeters = (currentRadius * 1000.0).toFloat()

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
                                alarmName = "" // Reset name for next alarm
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = currentColor),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Create",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Radius slider
                Slider(
                    value = currentRadius,
                    onValueChange = onRadiusChange,
                    valueRange = 0.1f..20f,
                    steps = 199,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = currentColor,
                        activeTrackColor = currentColor,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    track = { sliderState ->
                        SliderDefaults.Track(
                            sliderState = sliderState,
                            modifier = Modifier.height(6.dp),
                            colors = SliderDefaults.colors(
                                activeTrackColor = currentColor,
                                inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                    },
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .shadow(2.dp, CircleShape)
                                .background(MaterialTheme.colorScheme.surface, CircleShape)
                                .background(currentColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                            )
                        }
                    }
                )

                // Range labels and presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "100m",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val presets = listOf(0.5f, 1.0f, 2.0f, 5.0f)
                        presets.forEach { preset ->
                            val isSelected = kotlin.math.abs(currentRadius - preset) < 0.05f

                            FilterChip(
                                onClick = { onRadiusChange(preset) },
                                label = {
                                    Text(
                                        text = if (preset < 1f) "${(preset * 1000).toInt()}m" else "${preset.toInt()}k",
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                    )
                                },
                                selected = isSelected,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = currentColor.copy(alpha = 0.2f),
                                    selectedLabelColor = currentColor,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }

                    Text(
                        text = "20km",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.sp
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
                            focusedBorderColor = currentColor,
                            focusedLabelColor = currentColor
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNameDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = currentColor)
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