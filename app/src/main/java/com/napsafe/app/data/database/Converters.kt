package com.napsafe.app.data.database

import androidx.room.TypeConverter
import com.napsafe.app.data.model.AlarmType

class Converters {
    
    @TypeConverter
    fun fromAlarmType(alarmType: AlarmType): String {
        return alarmType.name
    }
    
    @TypeConverter
    fun toAlarmType(alarmType: String): AlarmType {
        return AlarmType.valueOf(alarmType)
    }
}