package com.napsafe.app.di;

import android.content.Context;
import com.napsafe.app.service.LocationService;
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
public final class AppModule_ProvideLocationServiceFactory implements Factory<LocationService> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideLocationServiceFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LocationService get() {
    return provideLocationService(contextProvider.get());
  }

  public static AppModule_ProvideLocationServiceFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideLocationServiceFactory(contextProvider);
  }

  public static LocationService provideLocationService(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLocationService(context));
  }
}
