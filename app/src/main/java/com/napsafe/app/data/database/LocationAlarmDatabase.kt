package com.napsafe.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.napsafe.app.data.model.LocationAlarm

@Database(
    entities = [LocationAlarm::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocationAlarmDatabase : RoomDatabase() {
    
    abstract fun locationAlarmDao(): LocationAlarmDao
    
    companion object {
        @Volatile
        private var INSTANCE: LocationAlarmDatabase? = null
        
        fun getDatabase(context: Context): LocationAlarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationAlarmDatabase::class.java,
                    "location_alarm_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}