package com.napsafe.app.ui.screens.alarms;

import com.napsafe.app.data.repository.LocationAlarmRepository;
import com.napsafe.app.service.GeofenceManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AlarmsViewModel_Factory implements Factory<AlarmsViewModel> {
  private final Provider<LocationAlarmRepository> repositoryProvider;

  private final Provider<GeofenceManager> geofenceManagerProvider;

  public AlarmsViewModel_Factory(Provider<LocationAlarmRepository> repositoryProvider,
      Provider<GeofenceManager> geofenceManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.geofenceManagerProvider = geofenceManagerProvider;
  }

  @Override
  public AlarmsViewModel get() {
    return newInstance(repositoryProvider.get(), geofenceManagerProvider.get());
  }

  public static AlarmsViewModel_Factory create(Provider<LocationAlarmRepository> repositoryProvider,
      Provider<GeofenceManager> geofenceManagerProvider) {
    return new AlarmsViewModel_Factory(repositoryProvider, geofenceManagerProvider);
  }

  public static AlarmsViewModel newInstance(LocationAlarmRepository repository,
      GeofenceManager geofenceManager) {
    return new AlarmsViewModel(repository, geofenceManager);
  }
}
