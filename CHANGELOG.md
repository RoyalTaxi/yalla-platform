# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.3.0] - 2026-01-06

### Changed
- **Resources Migration**
  - Migrated all drawable resources to `yalla-resources` library
  - Now depends on `uz.yalla:resources:1.0.0`
  - Icons are now loaded from yalla-resources instead of local composeResources

### Removed
- Local composeResources/drawable directory (icons moved to yalla-resources)

## [1.2.0] - 2026-01-06

### Added
- **Native Liquid Glass Effect (iOS)**
  - Restored iOS-native glass effect using `UIKitViewController` for button components
  - `NativeCircleIconButton` and `NativeSquircleIconButton` now use native iOS factories
  - iOS 26+ uses `.glassEffect(.regular.interactive())` for true liquid glass
  - iOS <26 falls back to `.ultraThinMaterial` for similar blur effect

### Changed
- **Button Architecture**
  - Converted buttons to `expect/actual` pattern for platform-specific implementations
  - iOS uses `LocalCircleIconButtonFactory` and `LocalSquircleIconButtonFactory` for native rendering
  - Android continues to use Compose implementation
  - Added `toAssetName()` extension for iOS asset catalog icon mapping

## [1.1.0] - 2025-01-05

### Changed
- **Button**
  - Migrated `NativeCircleIconButton` and `NativeSquircleIconButton` to use Compose Resources
  - Removed platform-specific implementations (Material Icons on Android, SF Symbols on iOS)
  - All icons now use PNG drawable resources for consistent appearance across platforms

### Added
- **Resources**
  - Added drawable icons: `ic_menu`, `ic_close`, `ic_done`, `ic_arrow_back`
  - Added focus icons: `ic_focus_location`, `ic_focus_route`, `ic_focus_origin`, `ic_focus_destination`
- **IconType**
  - Added `FOCUS_LOCATION`, `FOCUS_ROUTE`, `FOCUS_ORIGIN`, `FOCUS_DESTINATION` variants
  - Added `toDrawableResource()` extension for common icon mapping

### Removed
- Platform-specific `IconMapper` files (Android `toImageVector()`, iOS `toSFSymbol()`)
- Platform-specific button implementations

## [1.0.0] - 2024-12-30

### Added
- Initial stable release
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