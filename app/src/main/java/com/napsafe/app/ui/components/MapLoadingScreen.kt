package com.napsafe.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.napsafe.app.ui.theme.Primary
import com.napsafe.app.ui.theme.PrimaryVariant

@Composable
fun MapLoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_animation")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val fadeAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fade_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.08f),
                        PrimaryVariant.copy(alpha = 0.04f),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated location icon with professional pulsing
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                // Outer ring
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .alpha(fadeAlpha * 0.3f)
                        .background(
                            Primary.copy(alpha = 0.15f),
                            CircleShape
                        )
                )

                // Inner circle with icon
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Loading Map...",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier.alpha(fadeAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preparing your location experience",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.alpha(fadeAlpha * 0.8f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Clean progress indicator
            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(3.dp),
                color = Primary,
                trackColor = Primary.copy(alpha = 0.2f)
            )
        }
    }
}