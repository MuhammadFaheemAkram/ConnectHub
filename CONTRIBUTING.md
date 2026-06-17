# Contributing

Thanks for your interest in ConnectHub. This project is intentionally structured as a readable Android portfolio app, so contributions should preserve clarity, testability, and consistency with the existing architecture.

## Development Setup

1. Install Android Studio or a command-line Android SDK.
2. Use JDK 17 or newer.
3. Clone the repository.
4. Open the project in Android Studio or build from the terminal:

```bash
./gradlew :app:assembleDebug
```

## Before Opening A Pull Request

Run the checks that apply to your change:

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebugAndroidTest
```

If an emulator or device is available:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Code Style

- Keep Composables mostly stateless.
- Expose immutable `StateFlow` from ViewModels.
- Send UI events to ViewModels through functions.
- Keep Room, DataStore, and fake API access inside repositories.
- Use DTOs for fake remote models, entities for Room, domain models for business logic, and UI models only when they help the UI.
- Prefer small, focused tests near the behavior being changed.

## Pull Request Guidelines

- Keep changes focused.
- Update documentation when architecture, setup, or behavior changes.
- Add or update tests for meaningful behavior.
- Avoid unrelated refactors in feature PRs.
- Explain tradeoffs in the PR description when a choice is not obvious.

## Issues

Use the bug report or feature request templates when possible. Include device/emulator details for UI or Android runtime issues.
