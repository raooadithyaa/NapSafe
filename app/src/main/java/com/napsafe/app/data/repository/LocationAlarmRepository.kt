package com.napsafe.app.data.repository

import com.napsafe.app.data.database.LocationAlarmDao
import com.napsafe.app.data.model.LocationAlarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationAlarmRepository @Inject constructor(
    private val locationAlarmDao: LocationAlarmDao
) {
    
    fun getAllAlarms(): Flow<List<LocationAlarm>> = locationAlarmDao.getAllAlarms()
    
    fun getActiveAlarms(): Flow<List<LocationAlarm>> = locationAlarmDao.getActiveAlarms()
    
    suspend fun getAlarmById(id: String): LocationAlarm? = locationAlarmDao.getAlarmById(id)
    
    suspend fun insertAlarm(alarm: LocationAlarm) = locationAlarmDao.insertAlarm(alarm)
    
    suspend fun updateAlarm(alarm: LocationAlarm) = locationAlarmDao.updateAlarm(alarm)
    
    suspend fun deleteAlarm(alarm: LocationAlarm) = locationAlarmDao.deleteAlarm(alarm)
    
    suspend fun deleteAlarmById(id: String) = locationAlarmDao.deleteAlarmById(id)
    
    suspend fun updateAlarmStatus(id: String, isActive: Boolean) = 
        locationAlarmDao.updateAlarmStatus(id, isActive)
}