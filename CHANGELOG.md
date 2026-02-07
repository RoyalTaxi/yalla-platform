# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.2] - 2026-02-07

### Fixed
- **NativeSheet (Android)**
  - Fixed missing navigation bar padding by using `WindowInsets.ime.union(WindowInsets.navigationBars)`
  - Previous 2.1.1 broke navigation bar padding by only using IME insets

## [2.1.1] - 2026-02-07 [YANKED]

### Fixed
- **NativeSheet (Android)**
  - Added `contentWindowInsets = { WindowInsets.ime }` for proper keyboard handling
  - Sheet content now adjusts when keyboard appears, matching AnimatedSheet behavior
  - ⚠️ This version broke navigation bar padding - use 2.1.2 instead

## [2.1.0] - 2026-02-07

### Added
- **NativeSheet**
  - Added `onFullyExpanded` callback to notify when sheet animation completes
  - Focus can now be requested after sheet is fully visible, preventing janky behavior

### Changed
- **SheetPresenterFactory** (⚠️ Breaking)
  - Added `onPresented` callback parameter to `present` function
  - iOS apps must update their factory implementation to call `onPresented()` when presentation animation completes

## [1.10.19] - 2026-01-31

### Fixed
- **NativeSheet (Android)**
  - Ensure programmatic dismiss works when `dismissEnabled` is false
  - Use latest state in dismiss confirmation to avoid blocking hide after visibility changes

## [1.10.18] - 2026-01-29

### Added
- **NativeSheet**
  - Added `dismissEnabled` and `onDismissAttempt` to support non-dismissible sheets
  - Blocks interactive dismiss on iOS and triggers a callback when the user tries to dismiss
  - Prevents Android sheets from hiding while dismissal is disabled

## [1.10.13] - 2026-01-26

### Reverted
- **NativeSheet**
    - Reverted changes from 1.10.12 that didn't change padding behavior

## [1.10.12] - 2026-01-26

### Changed
- **NativeSheet**
    - Wrapped content in a box which respects `systemBarsPadding`


## [1.10.11] - 2026-01-26

### Changed
- **NativeSheet**
  - Added `isDark: Boolean?` parameter to allow callers to pass the app's theme preference
  - Falls back to `isSystemInDarkTheme()` when `isDark` is null
  - Fixes system bar appearance when user has explicitly set app theme (LIGHT/DARK) regardless of system setting

## [1.10.10] - 2026-01-26

### Fixed
- **NativeSheet (Android)**
  - Added theme-aware `ModalBottomSheetProperties` to control system bar appearance
  - Status bar and navigation bar icons now match app theme when sheet is open
  - Uses `isSystemInDarkTheme()` to detect current theme

## [1.10.9] - 2026-01-25

### Reverted
- **NativeSheet**
  - Reverted changes from 1.10.7/1.10.8 that broke sheet behavior

## [1.10.6] - 2026-01-24

### Fixed
- **NativeSheet**
  - Reset height measurement on each presentation to avoid stale detents
  - Always apply the first non-zero measured height for custom detents

## [1.10.5] - 2026-01-23

### Fixed
- **NativeSheet**
  - Native sheet background now updates when the theme changes

## [1.10.3] - 2026-01-23

### Changed
- **Native Buttons**
  - Removed `placedAsOverlay` so native buttons render inside the Compose layer

## [1.10.2] - 2026-01-23

### Changed
- **NativeSheet**
  - Compose sheet content now renders with a transparent host view (`opaque = false`)
  - Background color comes from the native sheet presentation layer

## [1.10.1] - 2026-01-23

### Changed
- **NativeSquircleIconButton**
  - Added `placedAsOverlay = true` for proper rendering with transparent backgrounds and native shader effects

## [1.10.0] - 2026-01-23

### Changed
- **Compose Multiplatform**
  - Updated to CMP 1.10.0 stable
- **NativeCircleIconButton**
  - Added `placedAsOverlay = true` for proper rendering with transparent backgrounds and native shader effects

## [1.9.6] - 2026-01-23

### Fixed
- **NativeSheet**
  - Fixed safe area not taking background color - now passes actual containerColor to native layer
  - Native view background color matches Compose background for full coverage

## [1.9.5] - 2026-01-23

### Fixed
- **NativeSheet**
  - Fixed sheet opening at full height - changed `fillMaxSize()` to `fillMaxWidth()`
  - Content height now properly measured for dynamic sheet sizing

## [1.9.4] - 2026-01-23

### Fixed
- **NativeSheet**
  - Background color now reactive to theme changes using `rememberUpdatedState`
  - Background now covers full view including safe areas using `fillMaxSize()`
  - Moved background application from SheetPresenter to NativeSheet for proper reactivity

## [1.9.3] - 2026-01-23

### Fixed
- **NativeSheet**
  - Apply background color within Compose layer using `Modifier.background()`
  - Native UIView background was being rendered behind Compose content

## [1.9.2] - 2026-01-23

### Fixed
- **NativeSheet**
  - Fixed background color not being applied correctly
  - Changed `containerColor.value.toLong()` to `containerColor.toArgb().toLong()` for proper ARGB format conversion

## [1.9.1] - 2026-01-23

### Fixed
- **NativeSheet**
  - Fixed transparent/glassy background by setting `opaque = true` on ComposeUIViewController
  - Previous `opaque = false` setting was overriding native background color enforcement

## [1.9.0] - 2026-01-23

### Changed
- **NativeSheet**
  - Added `backgroundColor` parameter to `SheetPresenterFactory.present()` for opaque sheet backgrounds
  - Background color flows from `NativeSheet.containerColor` through the factory to iOS native presentation
  - Eliminates transparency behind sheet content to match SwiftUI's `.background(Color.background)` behavior

## [1.8.1] - 2026-01-14

### Changed
- **NativeLoadingIndicator**
  - Added `backgroundColor` parameter to work around CMP iOS interop limitation
  - iOS interop views render under Compose canvas with transparent holes - background color prevents seeing through to window content

## [1.7.0] - 2026-01-13

### Changed
- **Dependencies**
  - Updated `snizzors` from `1.0.0` to `1.0.0-cmp1.10-alpha01` for Compose Multiplatform 1.10.x compatibility
  - Fixes `IrLinkageError: Function 'InteropView' can not be called` on iOS

## [1.6.0] - 2026-01-13

### Added
- **NativeLoadingIndicator**
  - Platform-native loading indicator component
  - iOS: Uses `UIActivityIndicatorView` with Snizzors overlay
  - Android: Uses Material3 `CircularProgressIndicator`
  - Supports custom color via `color` parameter

- **NativeWheelDatePicker**
  - Platform-native wheel date picker component
  - iOS: Uses `UIDatePicker` with wheel style via Snizzors overlay
  - Android: Uses `WheelDatePicker` from datetime-wheel-picker library
  - Supports `startDate`, `minDate`, `maxDate`, and `onDateChanged` callback

### Changed
- **iOS Native Components**
  - Migrated from `UIKitViewController` to `SnizzorsUIViewController` for proper overlay rendering
  - Native views now render **above** Compose content instead of below with hole-cutting
  - Eliminates white rectangle background issue on native components

### Dependencies
- Added `com.infiniteretry.snizzors:snizzors:1.0.0` for iOS overlay support
- Added `io.github.darkokoa:datetime-wheel-picker:1.1.0-alpha05-compose1.9` for Android date picker
- Added `org.jetbrains.kotlinx:kotlinx-datetime:0.7.1-0.6.x-compat` for date handling

## [1.5.6] - 2026-01-12

### Fixed
- **iOS NativeSheet**
  - Fixed "view is not in the window hierarchy" error when presenting sheets
  - Skip controllers that are being dismissed or have no window when finding parent
  - Renamed `topPresentedController()` to `topValidController()` with proper checks

## [1.5.5] - 2026-01-12

### Fixed
- **iOS NativeSheet**
  - Fixed bottom sheets not opening after a previous sheet was dismissed
  - Added `isVisible` key to `remember` block for `findKeyWindowRootController()`
  - Parent controller is now re-evaluated when sheet visibility changes, preventing stale references

## [1.4.7] - 2026-01-07

### Fixed
- **iOS Native Buttons**
  - Fixed icon not updating when `iconType` changes
  - Used `key(iconType)` to properly recreate UIKitViewController when icon changes

## [1.4.6] - 2026-01-07 [YANKED]

### Note
- UIButton.setImage approach didn't work - controller.view is not a UIButton

## [1.4.5] - 2026-01-07

### Fixed
- **iOS Native Buttons**
  - Restored alpha update with simplified lambda
  - Added viewinterop imports for newer API

## [1.4.4] - 2026-01-07 [YANKED]

### Note
- Removed update block entirely - buttons were too static

## [1.4.3] - 2026-01-07 [YANKED]

### Note
- Still crashed due to Compose's internal caching in the update block

## [1.4.2] - 2026-01-07 [YANKED]

### Note
- This version introduced `rememberUpdatedState` which caused EXC_BAD_ACCESS crashes - use 1.4.3 instead

## [1.4.1] - 2026-01-06

### Fixed
- **iOS Alpha Animation**
  - Fixed `NativeCircleIconButton` not responding to alpha changes on iOS
  - Now sets `viewController.view.alpha` directly in update block for proper fade animations

## [1.4.0] - 2026-01-06

### Fixed
- **iOS Icon Updates**
  - Fixed `NativeCircleIconButton` and `NativeSquircleIconButton` not updating icon on iOS when state changes
  - Added `update` block to `UIKitViewController` to handle recomposition
  - Used Objective-C runtime (`performSelector`) for dynamic method dispatch to Swift

### Changed
- **Button Architecture (iOS)**
  - Kotlin side now uses `NSSelectorFromString` and `performSelector` to call `updateIcon:` on the native controller
  - Swift `GlassButtonController` now uses `ObservableObject` pattern with `@Published` icon property
  - Added `IconUpdatable` protocol for type-safe icon updates

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
