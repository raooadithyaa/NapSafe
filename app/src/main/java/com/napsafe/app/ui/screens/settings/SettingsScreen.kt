package com.napsafe.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.napsafe.app.ui.theme.Primary
import com.napsafe.app.ui.theme.PrimaryVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Primary, PrimaryVariant)
                    )
                )
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Customize your alarm preferences",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SettingsSection(title = "Notifications") {
                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Push Notifications",
                        description = "Receive notifications when you reach your destination",
                        value = uiState.notificationsEnabled,
                        onValueChange = viewModel::updateNotificationsEnabled,
                        iconColor = Primary,
                        iconBg = Color(0xFFEEF2FF)
                    )

                    SettingItem(
                        icon = Icons.Default.VolumeUp,
                        title = "Sound Alerts",
                        description = "Play sound when alarm triggers",
                        value = uiState.soundEnabled,
                        onValueChange = viewModel::updateSoundEnabled,
                        iconColor = Color(0xFF10B981),
                        iconBg = Color(0xFFECFDF5)
                    )

                    SettingItem(
                        icon = Icons.Default.Vibration,
                        title = "Vibration",
                        description = "Vibrate device when alarm triggers",
                        value = uiState.vibrationEnabled,
                        onValueChange = viewModel::updateVibrationEnabled,
                        iconColor = Color(0xFFF59E0B),
                        iconBg = Color(0xFFFFFBEB)
                    )
                }
            }

            item {
                SettingsSection(title = "About") {
                    SettingItem(
                        icon = Icons.Default.Info,
                        title = "App Information",
                        description = "Version 1.0.0 • Location Alarm",
                        type = SettingType.INFO, // No arrow for info
                        iconColor = Color(0xFF6B7280),
                        iconBg = Color(0xFFF9FAFB)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Made with ❤️ for better location awareness",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            content()
        }
    }
}

enum class SettingType {
    SWITCH,
    INFO // No arrow, just info display
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    value: Boolean = false,
    onValueChange: (Boolean) -> Unit = {},
    type: SettingType = SettingType.SWITCH,
    iconColor: Color = Primary,
    iconBg: Color = Color(0xFFEEF2FF)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280)
            )
        }

        when (type) {
            SettingType.SWITCH -> {
                Switch(
                    checked = value,
                    onCheckedChange = onValueChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Primary
                    )
                )
            }
            SettingType.INFO -> {
                // No action element for info items
            }
        }
    }
}