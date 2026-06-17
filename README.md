# ConnectHub

ConnectHub is a modern open-source Android social feed app built for portfolio-quality architecture. It demonstrates Jetpack Compose, Material 3, MVVM, Kotlin Coroutines, Flow, StateFlow, Hilt, Room, DataStore, fake network APIs, offline caching, and a practical testing setup.

The product model is a simplified LinkedIn/Twitter-style app: users can authenticate with fake auth, browse a social feed, create posts, like and bookmark posts, comment, search, manage a profile, chat, view notifications, and persist app settings.

## Features

- Fake sign in and sign up with persisted session state
- Offline-first social feed with pull to refresh, pagination, likes, bookmarks, and cached posts
- Post detail, comments, comment creation, own-comment deletion, and create-post flow
- Debounced search for posts and people with recent searches persisted locally
- Bookmarks screen backed by Room
- Profile and edit-profile screens with local persistence
- Chat list, chat detail, send message flow, typing indicator, and simulated replies
- Notifications with categories, unread filtering, mark read, and mark all read
- DataStore-backed dark mode, dynamic color, notifications, and language settings
- Material 3 design system components and Navigation Compose app shell

## Screens

- Splash/session check
- Login and sign up
- Feed
- Post detail
- Create post
- Comments
- Search
- Bookmarks
- Notifications
- Chat list
- Chat detail
- Profile
- Edit profile
- Settings
- About

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- MVVM with immutable `StateFlow`
- Kotlin Coroutines and Flow
- Hilt
- Room
- DataStore Preferences
- Coil
- JUnit
- Compose UI tests
- Gradle Version Catalog

## Architecture

ConnectHub uses a clean MVVM-inspired architecture with clear separation between UI, domain, and data concerns.

```text
Compose UI
  -> ViewModel
  -> Use Case
  -> Domain Repository Interface
  -> Data Repository Implementation
  -> Fake API / Room / DataStore
```

Layer responsibilities:

- `feature`: Compose routes, mostly stateless screen composables, UI state, one-time effects, and ViewModels.
- `domain`: domain models, repository interfaces, and use cases.
- `data`: DTOs, Room entities, DAOs, mappers, fake API services, and repository implementations.
- `core`: design system, navigation, database, datastore, dispatchers, utilities, and testing helpers.
- `di`: Hilt bindings and providers.

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for the full architecture guide.

## Package Structure

```text
com.example.connecthub
├── ConnectHubApp.kt
├── MainActivity.kt
├── core
│   ├── common
│   ├── designsystem
│   ├── navigation
│   ├── network
│   ├── database
│   ├── datastore
│   └── testing
├── data
│   ├── auth
│   ├── feed
│   ├── comment
│   ├── chat
│   ├── notification
│   ├── profile
│   ├── search
│   ├── mapper
│   └── repository
├── domain
│   ├── model
│   ├── repository
│   └── usecase
├── feature
│   ├── auth
│   ├── feed
│   ├── postdetail
│   ├── createpost
│   ├── comments
│   ├── search
│   ├── bookmarks
│   ├── notifications
│   ├── chatlist
│   ├── chatdetail
│   ├── profile
│   └── settings
└── di
```

## How To Run

Requirements:

- Android Studio or command-line Android SDK
- JDK 17 or newer
- Android emulator or device for instrumented tests

Build the debug APK:

```bash
./gradlew :app:assembleDebug
```

Run unit tests:

```bash
./gradlew :app:testDebugUnitTest
```

Build Android test APKs:

```bash
./gradlew :app:assembleDebugAndroidTest
```

Run connected tests with an emulator or device attached:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Fake API

The app does not use a real backend. Fake Retrofit-style interfaces live in the data layer and are implemented with hard-coded responses, simulated network delay, and disabled-by-default error simulation through `FakeNetworkConfig`.

Implemented fake services:

- `AuthApiService`
- `FeedApiService`
- `CommentApiService`
- `ChatApiService`
- `NotificationApiService`

See [docs/API_SIMULATION.md](docs/API_SIMULATION.md).

## Offline Cache

Room stores cached posts, bookmarks, comments, recent searches, profile data, conversations, messages, and notifications. Repositories expose domain models through Flow and hide whether data came from fake network, Room, or DataStore. The UI observes local data first, and refresh actions update the cache.

## Tests

Tests are organized by runtime and responsibility:

- `src/test`: validation, use cases, repository logic, ViewModels, fake repository tests, and DataStore-backed settings tests.
- `src/androidTest`: Room DAO tests and Compose UI tests.
- `core/testing`: reusable test dispatcher utilities.

See [docs/TESTING.md](docs/TESTING.md).

## Screenshots

Screenshot placeholders live in [docs/SCREENSHOTS.md](docs/SCREENSHOTS.md). Add images there when publishing the repository.

## Roadmap

See [docs/ROADMAP.md](docs/ROADMAP.md).

## Contributing

Contributions are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md), [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md), and [SECURITY.md](SECURITY.md) before opening issues or pull requests.

## License

ConnectHub is released under the MIT License. See [LICENSE](LICENSE).
