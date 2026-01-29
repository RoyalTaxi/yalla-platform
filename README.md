# Yalla Platform

A Kotlin Multiplatform library providing platform-specific implementations for native UI components and navigation.

## Purpose

`yalla-platform` provides components that require platform-specific implementations to achieve truly native behavior:

- **Navigation** - Abstract navigation with platform-native controllers
- **System** - System bar appearance control
- **Sheets** - Native bottom sheet presentations
- **Buttons** - Native icon buttons with platform styling

## Installation

Add the GitHub Packages repository and dependency:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/RoyalTaxi/yalla-platform")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// build.gradle.kts
dependencies {
    implementation("uz.yalla:platform:1.0.2")
}
```

## Navigation

### Navigator Interface

The `Navigator` interface provides full navigation capabilities:

```kotlin
interface Navigator<S : Screen> {
    fun navigate(screen: S)
    fun back()
    fun setRoot(screen: S)
    fun popTo(screen: S, inclusive: Boolean = false)
    fun replaceFrom(fromScreen: S, toScreen: S)
}
```

### Screen Registry

Define screens with type-safe navigation:

```kotlin
// Define screens
sealed class AppScreen : Screen {
    data object Home : AppScreen() { override val route = "home" }
    data class Detail(val id: String) : AppScreen() { override val route = "detail/$id" }
}

// Create registry
val registry = screenRegistry<AppScreen>(start = AppScreen.Home) {
    screen(AppScreen.Home) { _, navigator ->
        HomeScreen(
            onNavigate = { navigator.navigate(AppScreen.Detail("123")) },
            onLogout = { navigator.setRoot(AppScreen.Login) }
        )
    }
    screen<AppScreen.Detail>(
        route = "detail/{id}",
        arguments = listOf(NavArgSpec("id", NavArgType.String)),
        parse = { args -> AppScreen.Detail(args.string("id")!!) }
    ) { screen, navigator ->
        DetailScreen(id = screen.id, onBack = { navigator.back() })
    }
}
```

### iOS Navigation Host

For iOS, use `IosNavigationHost` for a complete navigation solution:

```kotlin
fun AppRootController(): UIViewController {
    val registry = createAppScreenRegistry(...)

    val navResult = IosNavigationHost(
        registry = registry,
        startScreen = AppScreen.Home,
        viewControllerFactory = { screen, navigator ->
            val entry = registry.entryForScreen(screen)
            baseViewController {
                entry.content.Content(screen, navigator)
            }
        }
    )

    // Handle locale changes
    scope.launch {
        localeFlow.collect { navResult.rebuildBackstack() }
    }

    return navResult.navigationController
}
```

Features:
- `InteractiveNavigationController` with swipe-to-back gesture
- Automatic screen stack synchronization
- Full `Navigator` implementation
- `rebuildBackstack()` for locale changes

## Other Components

### System Bar Colors

Control status bar and navigation bar icon appearance:

```kotlin
// Based on background color luminance
SystemBarColors(statusBarColor = Color.White)

// Direct control
SystemBarColors(darkIcons = true)
```

### Native Sheet

Platform-native bottom sheet presentation:

```kotlin
NativeSheet(
    isVisible = showSheet,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    containerColor = System.color.backgroundBase,
    onDismissRequest = { showSheet = false },
    dismissEnabled = true,
    onDismissAttempt = { /* optional */ }
) {
    // Sheet content
}
```

### Native Buttons

Platform-native icon buttons:

```kotlin
// Circular button
NativeCircleIconButton(
    iconType = IconType.MENU,
    onClick = { /* handle click */ }
)

// Squircle button
NativeSquircleIconButton(
    iconType = IconType.CLOSE,
    onClick = { /* handle click */ }
)
```

## Platform Implementations

| Component | Android | iOS |
|-----------|---------|-----|
| Navigation | NavHostController | IosNavigationHost |
| System Bars | WindowInsetsController | UIStatusBarStyle |
| Sheet | ModalBottomSheet | UIViewController presentation |
| Buttons | Material3 IconButton | UIKitViewController with SF Symbols |

## Dependencies

- `uz.yalla:design` - Yalla Design System
- `androidx.navigation:navigation-compose` (Android)
- `androidx.core:core-ktx` (Android)

## License

Copyright 2024 Yalla. All rights reserved.
