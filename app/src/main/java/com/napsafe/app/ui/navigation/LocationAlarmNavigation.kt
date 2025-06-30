package com.napsafe.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.napsafe.app.ui.screens.alarms.AlarmsScreen
import com.napsafe.app.ui.screens.map.MapScreen
import com.napsafe.app.ui.screens.settings.SettingsScreen
import com.napsafe.app.ui.screens.settings.SettingsViewModel
import com.napsafe.app.ui.theme.NapSafeTheme
import com.napsafe.app.ui.theme.Primary

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Map : Screen("map", "Map", Icons.Default.LocationOn)
    object Alarms : Screen("alarms", "Alarms", Icons.Default.Notifications)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationAlarmNavigation() {
    val navController = rememberNavController()
    val items = listOf(Screen.Map, Screen.Alarms, Screen.Settings)

    // Get dark mode setting from SettingsViewModel
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    NapSafeTheme {
        Scaffold(
            bottomBar = {
                // Modern navigation bar with solid white background and clear tabs
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                ) {
                    Surface(
                        tonalElevation = 6.dp,
                        shadowElevation = 16.dp,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.White,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                                )
                                .navigationBarsPadding()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(72.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEachIndexed { idx, screen ->
                                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                            .background(
                                                if (selected) Primary.copy(alpha = 0.12f) else Color.Transparent,
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                            )
                                            .height(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                screen.icon,
                                                contentDescription = screen.title,
                                                tint = if (selected) Primary else Color(0xFF6B7280)
                                            )
                                            Text(
                                                screen.title,
                                                color = if (selected) Primary else Color(0xFF6B7280),
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            // Set bottom padding to exactly match nav bar height (72.dp + navigationBarsPadding)
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Map.route
                ) {
                    composable(Screen.Map.route) {
                        MapScreen(viewModel = hiltViewModel())
                    }
                    composable(Screen.Alarms.route) {
                        AlarmsScreen(viewModel = hiltViewModel())
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(viewModel = settingsViewModel)
                    }
                }
            }
        }
    }
}