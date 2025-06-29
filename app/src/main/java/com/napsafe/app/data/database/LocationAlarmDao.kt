package com.napsafe.app.data.database

import androidx.room.*
import com.napsafe.app.data.model.LocationAlarm
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationAlarmDao {
    
    @Query("SELECT * FROM location_alarms ORDER BY createdAt DESC")
    fun getAllAlarms(): Flow<List<LocationAlarm>>
    
    @Query("SELECT * FROM location_alarms WHERE isActive = 1")
    fun getActiveAlarms(): Flow<List<LocationAlarm>>
    
    @Query("SELECT * FROM location_alarms WHERE id = :id")
    suspend fun getAlarmById(id: String): LocationAlarm?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: LocationAlarm)
    
    @Update
    suspend fun updateAlarm(alarm: LocationAlarm)
    
    @Delete
    suspend fun deleteAlarm(alarm: LocationAlarm)
    
    @Query("DELETE FROM location_alarms WHERE id = :id")
    suspend fun deleteAlarmById(id: String)
    
    @Query("UPDATE location_alarms SET isActive = :isActive WHERE id = :id")
    suspend fun updateAlarmStatus(id: String, isActive: Boolean)
}