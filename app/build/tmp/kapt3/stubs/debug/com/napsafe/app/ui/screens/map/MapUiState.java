package com.napsafe.app.ui.screens.map;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b#\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0087\u0001\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0015J\u000b\u0010&\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u000fH\u00c6\u0003J\t\u0010(\u001a\u00020\u000fH\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010+\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J\u000f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000b0\bH\u00c6\u0003J\t\u0010-\u001a\u00020\rH\u00c6\u0003J\t\u0010.\u001a\u00020\u000fH\u00c6\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\t\u00100\u001a\u00020\u000fH\u00c6\u0003J\u008b\u0001\u00101\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u000f2\b\b\u0002\u0010\u0013\u001a\u00020\u000f2\b\b\u0002\u0010\u0014\u001a\u00020\u000fH\u00c6\u0001J\u0013\u00102\u001a\u00020\u000f2\b\u00103\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00104\u001a\u000205H\u00d6\u0001J\t\u00106\u001a\u00020\u0011H\u00d6\u0001R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u001cR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0014\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001cR\u0011\u0010\u0013\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001cR\u0011\u0010\u0012\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001cR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010!\u00a8\u00067"}, d2 = {"Lcom/napsafe/app/ui/screens/map/MapUiState;", "", "currentLocation", "Landroid/location/Location;", "tappedLocation", "Lcom/napsafe/app/data/model/LocationData;", "selectedLocation", "activeAlarms", "", "Lcom/napsafe/app/data/model/LocationAlarm;", "searchResults", "Lcom/napsafe/app/data/model/PlaceSearchResult;", "previewRadius", "", "isLoading", "", "error", "", "showSearchBottomSheet", "showAlarmSetupBottomSheet", "shouldCenterOnUser", "(Landroid/location/Location;Lcom/napsafe/app/data/model/LocationData;Lcom/napsafe/app/data/model/LocationData;Ljava/util/List;Ljava/util/List;FZLjava/lang/String;ZZZ)V", "getActiveAlarms", "()Ljava/util/List;", "getCurrentLocation", "()Landroid/location/Location;", "getError", "()Ljava/lang/String;", "()Z", "getPreviewRadius", "()F", "getSearchResults", "getSelectedLocation", "()Lcom/napsafe/app/data/model/LocationData;", "getShouldCenterOnUser", "getShowAlarmSetupBottomSheet", "getShowSearchBottomSheet", "getTappedLocation", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class MapUiState {
    @org.jetbrains.annotations.Nullable()
    private final android.location.Location currentLocation = null;
    @org.jetbrains.annotations.Nullable()
    private final com.napsafe.app.data.model.LocationData tappedLocation = null;
    @org.jetbrains.annotations.Nullable()
    private final com.napsafe.app.data.model.LocationData selectedLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.napsafe.app.data.model.LocationAlarm> activeAlarms = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.napsafe.app.data.model.PlaceSearchResult> searchResults = null;
    private final float previewRadius = 0.0F;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    private final boolean showSearchBottomSheet = false;
    private final boolean showAlarmSetupBottomSheet = false;
    private final boolean shouldCenterOnUser = false;
    
    @org.jetbrains.annotations.Nullable()
    public final android.location.Location component1() {
        return null;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.napsafe.app.data.model.LocationData component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.napsafe.app.data.model.LocationData component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.napsafe.app.data.model.LocationAlarm> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.napsafe.app.data.model.PlaceSearchResult> component5() {
        return null;
    }
    
    public final float component6() {
        return 0.0F;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.napsafe.app.ui.screens.map.MapUiState copy(@org.jetbrains.annotations.Nullable()
    android.location.Location currentLocation, @org.jetbrains.annotations.Nullable()
    com.napsafe.app.data.model.LocationData tappedLocation, @org.jetbrains.annotations.Nullable()
    com.napsafe.app.data.model.LocationData selectedLocation, @org.jetbrains.annotations.NotNull()
    java.util.List<com.napsafe.app.data.model.LocationAlarm> activeAlarms, @org.jetbrains.annotations.NotNull()
    java.util.List<com.napsafe.app.data.model.PlaceSearchResult> searchResults, float previewRadius, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean showSearchBottomSheet, boolean showAlarmSetupBottomSheet, boolean shouldCenterOnUser) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    public MapUiState(@org.jetbrains.annotations.Nullable()
    android.location.Location currentLocation, @org.jetbrains.annotations.Nullable()
    com.napsafe.app.data.model.LocationData tappedLocation, @org.jetbrains.annotations.Nullable()
    com.napsafe.app.data.model.LocationData selectedLocation, @org.jetbrains.annotations.NotNull()
    java.util.List<com.napsafe.app.data.model.LocationAlarm> activeAlarms, @org.jetbrains.annotations.NotNull()
    java.util.List<com.napsafe.app.data.model.PlaceSearchResult> searchResults, float previewRadius, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean showSearchBottomSheet, boolean showAlarmSetupBottomSheet, boolean shouldCenterOnUser) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.location.Location getCurrentLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.napsafe.app.data.model.LocationData getTappedLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.napsafe.app.data.model.LocationData getSelectedLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.napsafe.app.data.model.LocationAlarm> getActiveAlarms() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.napsafe.app.data.model.PlaceSearchResult> getSearchResults() {
        return null;
    }
    
    public final float getPreviewRadius() {
        return 0.0F;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public final boolean getShowSearchBottomSheet() {
        return false;
    }
    
    public final boolean getShowAlarmSetupBottomSheet() {
        return false;
    }
    
    public final boolean getShouldCenterOnUser() {
        return false;
    }
    
    public MapUiState() {
        super();
    }
}