# Phase 1 Summary: Project Foundation

## Phase Goal

Establish a compilable Android foundation with Compose, Material 3, Hilt, reusable UI components, and the navigation shell needed by later features.

## What Was Implemented

- Kotlin/Compose Android application module
- `ConnectHubApp` Hilt application class
- `MainActivity` Compose entry point
- Material 3 theme, colors, and typography
- Reusable buttons, text fields, avatar, top bar, bottom bar, loading/error/empty components, and post card
- Root, auth, and main route definitions
- Bottom navigation and modal navigation drawer
- Placeholder feature screens used while later phases were built
- Core package boundaries for common, design system, navigation, network, database, datastore, and testing

## Android Concepts Demonstrated

- Compose application setup
- Material 3 theming
- Stateless reusable Composables
- Navigation Compose route definitions
- Scaffold, bottom navigation, drawer, top app bar, and FAB
- Hilt application/activity integration
- Package-based clean architecture foundation

## Data Flow

Phase 1 focused on UI/navigation structure rather than real data:

```text
MainActivity
  -> ConnectHubTheme
  -> ConnectHubNavHost
  -> Root/Auth/Main destination
  -> Scaffold
  -> Placeholder or reusable screen component
```

## Important Files

- `ConnectHubApp.kt`: application class annotated with `@HiltAndroidApp`.
- `MainActivity.kt`: starts Compose and hosts the app theme/navigation.
- `core/designsystem/theme/Theme.kt`: light, dark, and dynamic color selection.
- `core/designsystem/component/`: reusable Material 3 components.
- `core/navigation/ConnectHubRoutes.kt`: sealed route definitions.
- `core/navigation/ConnectHubNavHost.kt`: root graph.
- `core/navigation/ConnectHubMainScaffold.kt`: app bar, drawer, bottom navigation, and FAB.
- `core/navigation/MainNavigation.kt`: main destination registration.

## Important Classes

### `ConnectHubApp`

Creates the Hilt application component and enables dependency injection for the app.

### `ConnectHubTheme`

Selects the color scheme and applies the project typography through Material 3.

### `ConnectHubMainScaffold`

Owns the main navigation controller and top-level navigation UI. It decides when to show bottom navigation, drawer navigation, back navigation, and the create-post FAB.

## Interview Notes

### Why build a design system early?

Shared components prevent visual and behavioral duplication. Later screens can focus on feature state while using consistent buttons, fields, cards, and states.

### Why define routes centrally?

Central route definitions reduce string duplication and provide one place for route arguments and destination titles.

### Why keep this as one Gradle module?

The package boundaries demonstrate architecture without adding module configuration overhead before project size or build performance requires it.

## Learning Checklist

- [ ] I can explain how `MainActivity` starts Compose.
- [ ] I can find the Material 3 theme and color schemes.
- [ ] I understand the route/screen design-system foundation.
- [ ] I can explain bottom navigation versus drawer navigation.
- [ ] I understand why Hilt needs an application class and Android entry point.

## Future Improvements

- Replace remaining generic About UI with a dedicated open-source/about screen.
- Add previews for more design-system components.
- Add screenshot tests for theme and shared components.
- Modularize the design system only if project scale justifies it.
