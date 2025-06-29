package com.napsafe.app.di

import android.content.Context
import androidx.room.Room
import com.napsafe.app.data.database.LocationAlarmDao
import com.napsafe.app.data.database.LocationAlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLocationAlarmDatabase(@ApplicationContext context: Context): LocationAlarmDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocationAlarmDatabase::class.java,
            "location_alarm_database"
        ).build()
    }

    @Provides
    fun provideLocationAlarmDao(database: LocationAlarmDatabase): LocationAlarmDao {
        return database.locationAlarmDao()
    }
}