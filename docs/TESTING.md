# Testing

ConnectHub uses focused tests that match the app architecture.

## Unit Tests

Unit tests live in `app/src/test`.

They cover:

- Auth validation
- Use cases
- Repository logic where practical
- ViewModels
- Fake repository behavior
- DataStore-backed settings repository

Run:

```bash
./gradlew :app:testDebugUnitTest
```

## ViewModel Tests

ViewModel tests use fake repositories and `MainDispatcherRule` so coroutine work runs deterministically. ViewModels are tested through public functions and exposed UI state.

Covered examples:

- Login and sign up
- Splash/session routing
- Feed loading, error, like, and bookmark behavior
- Post detail and comments
- Create post
- Search debounce
- Bookmarks
- Profile and edit profile
- Chat list and chat detail
- Notifications
- Settings

## Flow Tests

Flow behavior is tested by collecting emitted state in coroutine tests. Current tests use Kotlin Coroutines Test and fake repositories. Turbine can be added later if flows become more complex or need richer emission assertions.

Covered flows include:

- Feed observation
- Bookmarks observation
- Settings observation
- Message observation
- Recent searches

## Room Tests

Room DAO tests live in `app/src/androidTest`.

They use an in-memory Room database and cover:

- Insert/read posts
- Bookmark/unbookmark
- Comments insert/read/delete-own behavior
- Recent searches
- Profile persistence
- Conversations and messages
- Notifications read/unread state

Build Android test APKs:

```bash
./gradlew :app:assembleDebugAndroidTest
```

Run with an emulator or device attached:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Compose UI Tests

Compose UI tests live in `app/src/androidTest`.

They cover:

- Post detail content
- Create post validation/display states
- Chat detail message bubbles
- Notification filters
- Settings dark mode and dynamic color rows

Run:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Suggested Pre-PR Check

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebugAndroidTest
```

With a device or emulator:

```bash
./gradlew :app:connectedDebugAndroidTest
```
