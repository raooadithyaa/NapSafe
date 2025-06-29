package com.napsafe.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.napsafe.app.ui.theme.LocationAlarmTheme

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

    // SHARED ViewModels to preserve state across tab switches
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val mapViewModel: com.napsafe.app.ui.screens.map.MapViewModel = hiltViewModel()
    val alarmsViewModel: com.napsafe.app.ui.screens.alarms.AlarmsViewModel = hiltViewModel()
    
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    LocationAlarmTheme(darkTheme = settingsUiState.darkMode) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Map.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Map.route) {
                    MapScreen(
                        viewModel = mapViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
                composable(Screen.Alarms.route) {
                    AlarmsScreen(viewModel = alarmsViewModel)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(viewModel = settingsViewModel)
                }
            }
        }
    }
}