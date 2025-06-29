package com.napsafe.app.ui.screens.map;

import android.content.Context;
import com.napsafe.app.data.repository.LocationAlarmRepository;
import com.napsafe.app.service.GeofenceManager;
import com.napsafe.app.service.LocationService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class MapViewModel_Factory implements Factory<MapViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<LocationAlarmRepository> repositoryProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<GeofenceManager> geofenceManagerProvider;

  public MapViewModel_Factory(Provider<Context> contextProvider,
      Provider<LocationAlarmRepository> repositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<GeofenceManager> geofenceManagerProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.geofenceManagerProvider = geofenceManagerProvider;
  }

  @Override
  public MapViewModel get() {
    return newInstance(contextProvider.get(), repositoryProvider.get(), locationServiceProvider.get(), geofenceManagerProvider.get());
  }

  public static MapViewModel_Factory create(Provider<Context> contextProvider,
      Provider<LocationAlarmRepository> repositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<GeofenceManager> geofenceManagerProvider) {
    return new MapViewModel_Factory(contextProvider, repositoryProvider, locationServiceProvider, geofenceManagerProvider);
  }

  public static MapViewModel newInstance(Context context, LocationAlarmRepository repository,
      LocationService locationService, GeofenceManager geofenceManager) {
    return new MapViewModel(context, repository, locationService, geofenceManager);
  }
}
