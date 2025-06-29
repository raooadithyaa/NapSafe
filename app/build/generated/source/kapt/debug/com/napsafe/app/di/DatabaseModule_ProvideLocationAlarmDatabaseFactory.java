package com.napsafe.app.di;

import android.content.Context;
import com.napsafe.app.data.database.LocationAlarmDatabase;
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
public final class DatabaseModule_ProvideLocationAlarmDatabaseFactory implements Factory<LocationAlarmDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideLocationAlarmDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LocationAlarmDatabase get() {
    return provideLocationAlarmDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideLocationAlarmDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideLocationAlarmDatabaseFactory(contextProvider);
  }

  public static LocationAlarmDatabase provideLocationAlarmDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLocationAlarmDatabase(context));
  }
}
