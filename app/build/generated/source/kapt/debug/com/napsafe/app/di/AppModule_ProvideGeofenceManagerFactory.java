package com.napsafe.app.di;

import android.content.Context;
import com.napsafe.app.service.GeofenceManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideGeofenceManagerFactory implements Factory<GeofenceManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideGeofenceManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public GeofenceManager get() {
    return provideGeofenceManager(contextProvider.get());
  }

  public static AppModule_ProvideGeofenceManagerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideGeofenceManagerFactory(contextProvider);
  }

  public static GeofenceManager provideGeofenceManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGeofenceManager(context));
  }
}
