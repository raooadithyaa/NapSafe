# Location Alarm App

A beautiful native Android app built with Kotlin and Jetpack Compose that allows users to set location-based alarms.

## Features

- 🗺️ **Interactive Google Maps** - Search and select locations with ease
- 📍 **Location-based Alarms** - Set alarms that trigger when you reach a destination
- 🔔 **Multiple Alert Types** - Choose from notifications, sounds, or vibrations
- 📱 **Modern UI** - Beautiful Material Design 3 interface with gradients and animations
- ⚙️ **Customizable Settings** - Adjust alarm preferences and app behavior
- 🎯 **Geofencing** - Efficient background location monitoring
- 💾 **Local Storage** - All data stored securely on device using Room database

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Dependency Injection**: Hilt
- **Database**: Room
- **Maps**: Google Maps SDK
- **Location Services**: Google Play Services Location API
- **Background Processing**: WorkManager & Geofencing API

## Setup

1. Clone the repository
2. Get a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com/)
3. Add your API key to `secrets.properties`:
   ```
   MAPS_API_KEY=your_api_key_here
   ```
4. Enable the following APIs in Google Cloud Console:
   - Maps SDK for Android
   - Places API
   - Geocoding API
5. Build and run the app

## Architecture

The app follows clean architecture principles with:

- **UI Layer**: Jetpack Compose screens and components
- **Domain Layer**: ViewModels and use cases
- **Data Layer**: Repository pattern with Room database and Google Services

## Permissions

The app requires the following permissions:
- `ACCESS_FINE_LOCATION` - For precise location tracking
- `ACCESS_COARSE_LOCATION` - For general location access
- `ACCESS_BACKGROUND_LOCATION` - For geofencing when app is in background
- `POST_NOTIFICATIONS` - For alarm notifications
- `VIBRATE` - For vibration alerts
- `WAKE_LOCK` - For reliable alarm triggering

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License.