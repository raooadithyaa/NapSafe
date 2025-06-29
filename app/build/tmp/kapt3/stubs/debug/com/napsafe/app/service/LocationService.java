package com.napsafe.app.service;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0011J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000fJ\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0087@\u00a2\u0006\u0002\u0010\u001aJ\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001eJ.\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00112\u0006\u0010#\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u000fJ\u001c\u0010&\u001a\b\u0012\u0004\u0012\u00020(0\'2\u0006\u0010)\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010*\u001a\u00020 2\u0006\u0010+\u001a\u00020\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000b\u00a8\u0006,"}, d2 = {"Lcom/napsafe/app/service/LocationService;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "placesClient", "Lcom/google/android/libraries/places/api/net/PlacesClient;", "kotlin.jvm.PlatformType", "getPlacesClient", "()Lcom/google/android/libraries/places/api/net/PlacesClient;", "placesClient$delegate", "Lkotlin/Lazy;", "calculateDistance", "", "lat1", "", "lon1", "lat2", "lon2", "formatDistance", "", "meters", "getCurrentLocation", "Landroid/location/Location;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPlaceDetails", "Lcom/napsafe/app/data/model/LocationData;", "placeId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isWithinRadius", "", "currentLat", "currentLon", "targetLat", "targetLon", "radiusInMeters", "searchPlaces", "", "Lcom/napsafe/app/data/model/PlaceSearchResult;", "query", "validateRadius", "radiusInKm", "app_debug"})
public final class LocationService {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy placesClient$delegate = null;
    
    @javax.inject.Inject()
    public LocationService(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final com.google.android.libraries.places.api.net.PlacesClient getPlacesClient() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCurrentLocation(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super android.location.Location> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchPlaces(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.napsafe.app.data.model.PlaceSearchResult>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getPlaceDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String placeId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.napsafe.app.data.model.LocationData> $completion) {
        return null;
    }
    
    /**
     * Calculate PRECISE distance between two points using Android's built-in method
     * This is the most accurate method for geographic calculations
     * Returns distance in meters with high precision
     */
    public final float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0F;
    }
    
    /**
     * Check if a location is within the specified radius of a target location
     * Uses PRECISE distance calculation with geographic accuracy
     */
    public final boolean isWithinRadius(double currentLat, double currentLon, double targetLat, double targetLon, float radiusInMeters) {
        return false;
    }
    
    /**
     * Validate radius value for geographic accuracy
     */
    public final boolean validateRadius(float radiusInKm) {
        return false;
    }
    
    /**
     * Get formatted distance string for UI display
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDistance(float meters) {
        return null;
    }
}