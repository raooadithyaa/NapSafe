package com.napsafe.app.data.repository;

import com.napsafe.app.data.database.LocationAlarmDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class LocationAlarmRepository_Factory implements Factory<LocationAlarmRepository> {
  private final Provider<LocationAlarmDao> locationAlarmDaoProvider;

  public LocationAlarmRepository_Factory(Provider<LocationAlarmDao> locationAlarmDaoProvider) {
    this.locationAlarmDaoProvider = locationAlarmDaoProvider;
  }

  @Override
  public LocationAlarmRepository get() {
    return newInstance(locationAlarmDaoProvider.get());
  }

  public static LocationAlarmRepository_Factory create(
      Provider<LocationAlarmDao> locationAlarmDaoProvider) {
    return new LocationAlarmRepository_Factory(locationAlarmDaoProvider);
  }

  public static LocationAlarmRepository newInstance(LocationAlarmDao locationAlarmDao) {
    return new LocationAlarmRepository(locationAlarmDao);
  }
}
