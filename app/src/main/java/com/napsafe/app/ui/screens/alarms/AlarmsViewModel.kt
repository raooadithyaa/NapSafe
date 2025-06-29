package com.napsafe.app.ui.screens.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napsafe.app.data.model.LocationAlarm
import com.napsafe.app.data.repository.LocationAlarmRepository
import com.napsafe.app.service.GeofenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlarmsUiState(
    val alarms: List<LocationAlarm> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    private val repository: LocationAlarmRepository,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmsUiState())
    val uiState: StateFlow<AlarmsUiState> = _uiState.asStateFlow()

    init {
        observeAlarms()
    }

    private fun observeAlarms() {
        viewModelScope.launch {
            repository.getAllAlarms().collect { alarms ->
                _uiState.update { it.copy(alarms = alarms) }
            }
        }
    }

    fun deleteAlarm(alarm: LocationAlarm) {
        viewModelScope.launch {
            try {
                repository.deleteAlarm(alarm)
                geofenceManager.removeGeofence(alarm.id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun toggleAlarmStatus(alarm: LocationAlarm) {
        viewModelScope.launch {
            try {
                val updatedAlarm = alarm.copy(isActive = !alarm.isActive)
                repository.updateAlarm(updatedAlarm)

                if (updatedAlarm.isActive) {
                    geofenceManager.addGeofence(updatedAlarm)
                } else {
                    geofenceManager.removeGeofence(alarm.id)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}