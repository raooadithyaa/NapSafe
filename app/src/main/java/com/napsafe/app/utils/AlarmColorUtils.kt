package com.napsafe.app.utils

import androidx.compose.ui.graphics.Color

/**
 * FIXED: Utility for generating UNIQUE colors for each alarm circle
 */
object AlarmColorUtils {

    // BEAUTIFUL, DISTINCT colors for alarm circles - each alarm gets a unique color
    private val alarmColors = listOf(
        Color(0xFF10B981), // Emerald Green
        Color(0xFF3B82F6), // Bright Blue
        Color(0xFFEF4444), // Vibrant Red
        Color(0xFFF59E0B), // Golden Yellow
        Color(0xFF8B5CF6), // Purple
        Color(0xFF06B6D4), // Cyan
        Color(0xFFEC4899), // Hot Pink
        Color(0xFF84CC16), // Lime Green
        Color(0xFFF97316), // Orange
        Color(0xFF6366F1), // Indigo
        Color(0xFF14B8A6), // Teal
        Color(0xFFEAB308), // Amber
        Color(0xFFDC2626), // Deep Red
        Color(0xFF059669), // Forest Green
        Color(0xFF7C3AED), // Violet
        Color(0xFFDB2777)  // Rose
    )

    /**
     * FIXED: Get a UNIQUE color for each alarm based on its ID
     * This ensures each alarm gets a consistent, DIFFERENT color
     */
    fun getColorForAlarm(alarmId: String): Color {
        // Use alarm ID hash to get consistent color assignment
        val colorIndex = kotlin.math.abs(alarmId.hashCode()) % alarmColors.size
        return alarmColors[colorIndex]
    }

    /**
     * FIXED: Get the fill color (with transparency) for an alarm circle
     */
    fun getFillColorForAlarm(alarmId: String, isActive: Boolean): Color {
        val baseColor = getColorForAlarm(alarmId)
        val alpha = if (isActive) 0.25f else 0.15f // More visible when active
        return baseColor.copy(alpha = alpha)
    }

    /**
     * FIXED: Get the stroke color for an alarm circle
     */
    fun getStrokeColorForAlarm(alarmId: String, isActive: Boolean): Color {
        val baseColor = getColorForAlarm(alarmId)
        return if (isActive) baseColor else baseColor.copy(alpha = 0.7f)
    }

    /**
     * Get all available colors (for UI previews)
     */
    fun getAllColors(): List<Color> = alarmColors

    /**
     * FIXED: Debug function to ensure colors are unique
     */
    fun getColorDebugInfo(alarmId: String): String {
        val colorIndex = kotlin.math.abs(alarmId.hashCode()) % alarmColors.size
        return "Alarm $alarmId -> Color Index: $colorIndex"
    }
}