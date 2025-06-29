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
import com.napsafe.app.ui.theme.LocationAlarmTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // PREMIUM: Install beautiful animated splash screen FIRST
        val splashScreen = installSplashScreen()

        // PREMIUM: Keep splash screen visible for perfect timing
        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // PREMIUM: Set beautiful gradient background IMMEDIATELY
        window.setBackgroundDrawableResource(R.drawable.splash_background)

        setContent {
            LocationAlarmTheme {
                val systemUiController = rememberSystemUiController()

                LaunchedEffect(Unit) {
                    // PREMIUM: Perfect timing for splash screen
                    delay(1500) // Show beautiful splash for 1.5 seconds

                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )

                    // PREMIUM: Hide splash screen after perfect timing
                    keepSplashOnScreen = false
                }

                // PREMIUM: Immediate background to prevent any white flash
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