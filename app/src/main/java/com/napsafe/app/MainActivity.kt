package com.napsafe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.napsafe.app.ui.navigation.LocationAlarmNavigation
import com.napsafe.app.ui.theme.NapSafeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // PERFORMANCE: Optimized splash screen timing
        val splashScreen = installSplashScreen()

        // PERFORMANCE: Shorter splash duration for faster app startup
        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // PERFORMANCE: Set background immediately to prevent white flash
        window.setBackgroundDrawableResource(R.color.app_background)

        setContent {
            NapSafeTheme {
                val systemUiController = rememberSystemUiController()

                LaunchedEffect(Unit) {
                    // PERFORMANCE: Reduced splash timing for faster startup
                    delay(800) // Reduced from 1500ms to 800ms

                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )

                    keepSplashOnScreen = false
                }

                // PERFORMANCE: Immediate background to prevent white flash
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationAlarmNavigation()
                }
            }
        }
    }
}