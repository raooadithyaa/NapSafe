package com.napsafe.app.ui.screens.alarms;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\b\u0010\u0012\u001a\u00020\u000fH\u0002J\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0014"}, d2 = {"Lcom/napsafe/app/ui/screens/alarms/AlarmsViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/napsafe/app/data/repository/LocationAlarmRepository;", "geofenceManager", "Lcom/napsafe/app/service/GeofenceManager;", "(Lcom/napsafe/app/data/repository/LocationAlarmRepository;Lcom/napsafe/app/service/GeofenceManager;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/napsafe/app/ui/screens/alarms/AlarmsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "deleteAlarm", "", "alarm", "Lcom/napsafe/app/data/model/LocationAlarm;", "observeAlarms", "toggleAlarmStatus", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AlarmsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.napsafe.app.data.repository.LocationAlarmRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.napsafe.app.service.GeofenceManager geofenceManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.napsafe.app.ui.screens.alarms.AlarmsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.napsafe.app.ui.screens.alarms.AlarmsUiState> uiState = null;
    
    @javax.inject.Inject()
    public AlarmsViewModel(@org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.repository.LocationAlarmRepository repository, @org.jetbrains.annotations.NotNull()
    com.napsafe.app.service.GeofenceManager geofenceManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.napsafe.app.ui.screens.alarms.AlarmsUiState> getUiState() {
        return null;
    }
    
    private final void observeAlarms() {
    }
    
    public final void deleteAlarm(@org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.model.LocationAlarm alarm) {
    }
    
    public final void toggleAlarmStatus(@org.jetbrains.annotations.NotNull()
    com.napsafe.app.data.model.LocationAlarm alarm) {
    }
}