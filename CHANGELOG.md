# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-12-29

### Added
- Initial stable release
- **Navigation**
  - `Navigator` interface for type-safe navigation
  - `Screen` interface with route support
  - `ScreenRegistry` for screen registration with DSL builder
  - `NavArgs` for navigation arguments
  - `PlatformNavController` (NavHostController on Android, UINavigationController on iOS)
  - `PlatformNavigator` with platform-specific implementations
- **System**
  - `SystemBarColors` for status/navigation bar icon control
- **Sheet**
  - `NativeSheet` for platform-native bottom sheet presentation
  - `SheetPresenter` for iOS sheet management
  - `SheetPresenterFactory` for custom iOS sheet presentation
- **Button**
  - `NativeCircleIconButton` with circular styling
  - `NativeSquircleIconButton` with squircle styling
  - `IconType` enum (MENU, LOCATION, CLOSE, DONE, Back)
- **Utilities**
  - `Color.toUIColor()` extension for iOS
  - Icon mappers (ImageVector for Android, SF Symbols for iOS)
  - Factory composition locals for iOS interop
