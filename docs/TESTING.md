# Testing

ConnectHub uses local JVM tests for fast logic feedback and Android instrumented tests for Room and Compose behavior.

## Test Source Sets

```text
app/src/test/         JVM unit tests
app/src/androidTest/  Android instrumented, Room, and Compose UI tests
```

## Current Dependencies

- JUnit 4
- Kotlin Coroutines Test
- AndroidX Test JUnit
- Espresso core
- Compose UI test JUnit 4
- In-memory Room databases for DAO tests

Turbine and MockK are not currently configured. Flow tests use coroutine test utilities and purpose-built fake repositories.

## Unit Tests

Unit tests validate logic without an emulator.

Current coverage includes:

- Login and sign-up validation
- Login, sign-up, splash, and session ViewModels
- Feed use cases, repository behavior, loading, error, like, and bookmark state
- Create-post and add-comment use cases
- Post detail, comments, and create-post ViewModels
- Search debounce and recent searches
- Bookmarks, profile, and edit-profile ViewModels
- Chat list and chat detail ViewModels
- Notifications and settings ViewModels
- DataStore-backed `SettingsRepositoryImpl`

Run:

```bash
./gradlew :app:testDebugUnitTest
```

## ViewModel Tests

ViewModel tests replace repository interfaces with deterministic fakes. `MainDispatcherRule` replaces `Dispatchers.Main` with a test dispatcher.

Typical pattern:

```text
Create fake repository
  -> construct use cases
  -> construct ViewModel
  -> call a public intent function
  -> advance test scheduler
  -> assert UI StateFlow or one-time effect
```

This verifies the ViewModel contract without Compose or Android framework rendering.

## Repository And Use Case Tests

Use-case tests verify delegation and small input rules, such as trimming post, comment, search, or message text.

`FeedRepositoryImplTest` uses fake API and DAO implementations to verify cache writes and local like/bookmark state. `SettingsRepositoryImplTest` uses a real temporary Preferences DataStore file.

## Flow Tests

Flow behavior is exercised through `runTest`, StateFlow fakes, `first`, and virtual time.

Examples:

- Search waits for its debounce before querying.
- Room-backed fake repositories update ViewModel state after mutations.
- Settings updates are observed after DataStore writes.
- Message lists update after send/reply behavior.

Turbine would be a useful future addition for asserting longer emission sequences.

## Room Tests

DAO tests create `ConnectHubDatabase` with `Room.inMemoryDatabaseBuilder`.

Current DAO tests cover:

- Insert/read cached posts
- Bookmark and unbookmark
- Search posts and filter by author
- Insert/read comments and delete only own comments
- Insert/read/clear recent searches
- Upsert and observe profile
- Insert/read conversations and messages
- Update conversation preview
- Mark one or all notifications as read

Build the Android test APK:

```bash
./gradlew :app:assembleDebugAndroidTest
```

Run on an attached emulator or device:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Compose UI Tests

Compose tests render stateless content Composables with explicit UI state.

Current checks include:

- Post detail displays content and comments preview
- Create-post button enabled/disabled behavior
- Chat detail displays message bubbles and input
- Notification filters appear
- Settings displays dark mode and dynamic color controls

The tests intentionally focus on screen contracts rather than recreating full end-to-end navigation.

## Lint And Build Verification

Build:

```bash
./gradlew :app:assembleDebug
```

Lint:

```bash
./gradlew :app:lintDebug
```

The GitHub Actions workflow runs build, unit tests, and lint for pushes and pull requests targeting `main`.

## Recommended Pre-Commit Sequence

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebugAndroidTest
./gradlew :app:lintDebug
```

With an emulator or device:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Useful Tests To Add Next

- Navigation tests for auth-to-main graph switching
- Compose interactions for feed like/bookmark actions
- Migration tests using exported Room schemas
- Repository failure tests for chat and notifications
- Accessibility semantics checks
- Screenshot or visual regression tests
- Turbine-based tests for exact Flow emission order
