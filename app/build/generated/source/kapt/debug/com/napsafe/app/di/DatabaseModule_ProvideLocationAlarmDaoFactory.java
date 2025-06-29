package com.napsafe.app.di;

import com.napsafe.app.data.database.LocationAlarmDao;
import com.napsafe.app.data.database.LocationAlarmDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class DatabaseModule_ProvideLocationAlarmDaoFactory implements Factory<LocationAlarmDao> {
  private final Provider<LocationAlarmDatabase> databaseProvider;

  public DatabaseModule_ProvideLocationAlarmDaoFactory(
      Provider<LocationAlarmDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public LocationAlarmDao get() {
    return provideLocationAlarmDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideLocationAlarmDaoFactory create(
      Provider<LocationAlarmDatabase> databaseProvider) {
    return new DatabaseModule_ProvideLocationAlarmDaoFactory(databaseProvider);
  }

  public static LocationAlarmDao provideLocationAlarmDao(LocationAlarmDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLocationAlarmDao(database));
  }
}
