package com.napsafe.app.utils;

/**
 * Geographic utility functions for precise distance and radius calculations
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J&\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000bJ.\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000bJ\u000e\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u000bJ\u000e\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000bJ\u000e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/napsafe/app/utils/GeographicUtils;", "", "()V", "EARTH_RADIUS_METERS", "", "calculateDistance", "lat1", "lon1", "lat2", "lon2", "calculatePreciseDistance", "", "calculateZoomForRadius", "radiusInMeters", "formatDistance", "", "meters", "isWithinRadius", "", "currentLat", "currentLon", "targetLat", "targetLon", "kilometersToMeters", "kilometers", "metersToKilometers", "validateRadius", "radiusInKm", "app_debug"})
public final class GeographicUtils {
    private static final double EARTH_RADIUS_METERS = 6378137.0;
    @org.jetbrains.annotations.NotNull()
    public static final com.napsafe.app.utils.GeographicUtils INSTANCE = null;
    
    private GeographicUtils() {
        super();
    }
    
    /**
     * Calculate precise distance between two geographic points using Haversine formula
     * Returns distance in meters with high precision
     */
    public final double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0;
    }
    
    /**
     * Calculate precise distance using Android's Location.distanceBetween
     * This is the most accurate method for geographic calculations
     */
    public final float calculatePreciseDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0F;
    }
    
    /**
     * Check if a point is within a circular radius of a target location
     * Uses precise geographic distance calculation
     */
    public final boolean isWithinRadius(double currentLat, double currentLon, double targetLat, double targetLon, float radiusInMeters) {
        return false;
    }
    
    /**
     * Convert kilometers to meters with precision
     */
    public final float kilometersToMeters(float kilometers) {
        return 0.0F;
    }
    
    /**
     * Convert meters to kilometers with precision
     */
    public final float metersToKilometers(float meters) {
        return 0.0F;
    }
    
    /**
     * Validate that radius is within reasonable bounds for location alarms
     */
    public final boolean validateRadius(float radiusInKm) {
        return false;
    }
    
    /**
     * Get a human-readable distance string
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDistance(float meters) {
        return null;
    }
    
    /**
     * Calculate the approximate zoom level needed to show a radius on the map
     * This helps with automatic map zoom when creating alarms
     */
    public final float calculateZoomForRadius(float radiusInMeters) {
        return 0.0F;
    }
}