package com.napsafe.app.ui.screens.map;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B)\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0016\u001a\u00020\u0017J\u0010\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u0006\u0010\u001b\u001a\u00020\u0017J\u0006\u0010\u001c\u001a\u00020\u0017J\u000e\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020\u0017J\u0006\u0010!\u001a\u00020\u0017J\b\u0010\"\u001a\u00020\u0017H\u0002J\b\u0010#\u001a\u00020\u0017H\u0014J\u000e\u0010$\u001a\u00020\u00172\u0006\u0010%\u001a\u00020&J\u0006\u0010\'\u001a\u00020\u0017J\u000e\u0010(\u001a\u00020\u00172\u0006\u0010)\u001a\u00020*J\u0006\u0010+\u001a\u00020\u0017J\u000e\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020.J\u0006\u0010/\u001a\u00020\u0017J\u000e\u00100\u001a\u00020\u00172\u0006\u00101\u001a\u00020\u001fJ\b\u00102\u001a\u00020\u0017H\u0007J\u0006\u00103\u001a\u00020\u0017J\u0010\u00104\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u000e\u00105\u001a\u00020\u00172\u0006\u00106\u001a\u000207J\u000e\u00108\u001a\u00020\u00172\u0006\u0010%\u001a\u00020&R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u00069"}, d2 = {"Lcom/napsafe/app/ui/screens/map/MapViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "repository", "Lcom/napsafe/app/data/repository/LocationAlarmRepository;", "locationService", "Lcom/napsafe/app/service/LocationService;", "geofenceManager", "Lcom/napsafe/app/service/GeofenceManager;", "(Landroid/content/Context;Lcom/napsafe/app/data/repository/LocationAlarmRepository;Lcom/napsafe/app/service/LocationService;Lcom/napsafe/app/service/GeofenceManager;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/napsafe/app/ui/screens/map/MapUiState;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "locationCallback", "Lcom/google/android/gms/location/LocationCallback;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "centerOnUserLocation", "", "checkProximityToAlarms", "currentLocation", "Landroid/location/Location;", "clearSelectedLocation", "clearTappedLocation", "createAlarm", "alarm", "Lcom/napsafe/app/data/model/LocationAlarm;", "createAlarmForTappedLocation", "hideSearchBottomSheet", "observeActiveAlarms", "onCleared", "onMapTap", "latLng", "Lcom/google/android/gms/maps/model/LatLng;", "onUserLocationCentered", "searchPlaces", "query", "", "selectCurrentLocation", "selectPlace", "place", "Lcom/napsafe/app/data/model/PlaceSearchResult;", "showSearchBottomSheet", "simulateLocationForTesting", "targetAlarm", "startLocationUpdates", "stopLocationUpdates", "triggerAlarm", "updatePreviewRadius", "radius", "", "updateTappedLocation", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MapViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.napsafe.app.data.repository.LocationAlarmRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.napsafe.app.service.LocationService locationService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.napsafe.app.service.GeofenceManager geofenceManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.napsafe.app.ui.screens.map.MapUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.napsafe.app.ui.screens.map.MapUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.location.LocationCallback locationCallback;
    
    @javax.inject.Inject()
    public MapViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.repository.LocationAlarmRepository repository, @org.jetbrains.annotations.NotNull()
    com.napsafe.app.service.LocationService locationService, @org.jetbrains.annotations.NotNull()
    com.napsafe.app.service.GeofenceManager geofenceManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.napsafe.app.ui.screens.map.MapUiState> getUiState() {
        return null;
    }
    
    private final void observeActiveAlarms() {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    public final void startLocationUpdates() {
    }
    
    public final void stopLocationUpdates() {
    }
    
    public final void onMapTap(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.model.LatLng latLng) {
    }
    
    public final void createAlarmForTappedLocation() {
    }
    
    public final void updatePreviewRadius(float radius) {
    }
    
    public final void centerOnUserLocation() {
    }
    
    public final void onUserLocationCentered() {
    }
    
    public final void searchPlaces(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void selectPlace(@org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.model.PlaceSearchResult place) {
    }
    
    public final void selectCurrentLocation() {
    }
    
    public final void clearSelectedLocation() {
    }
    
    public final void showSearchBottomSheet() {
    }
    
    public final void hideSearchBottomSheet() {
    }
    
    public final void createAlarm(@org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.model.LocationAlarm alarm) {
    }
    
    public final void simulateLocationForTesting(@org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.model.LocationAlarm targetAlarm) {
    }
    
    private final void checkProximityToAlarms(android.location.Location currentLocation) {
    }
    
    private final void triggerAlarm(com.napsafe.app.data.model.LocationAlarm alarm) {
    }
    
    public final void clearTappedLocation() {
    }
    
    public final void updateTappedLocation(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.model.LatLng latLng) {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}