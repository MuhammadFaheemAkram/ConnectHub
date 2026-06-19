# Interview Notes

These questions and answers use ConnectHub as a concrete discussion project. Good interview answers should explain both the chosen design and its tradeoffs.

## Architecture

### Why did you use MVVM?

MVVM fits Compose because the UI can render immutable state while a ViewModel owns asynchronous work and user intents. In ConnectHub, screens are mostly stateless, ViewModels expose StateFlow, and repositories hide Room, DataStore, and fake API details. This separation improves testability and keeps configuration changes from resetting screen logic.

### Is this strict Clean Architecture?

It is clean architecture-inspired rather than doctrinaire. The project has UI, domain, and data boundaries, but it remains a single Gradle module to avoid modularization overhead for a portfolio-sized app. Package dependencies and interfaces enforce the most valuable boundaries.

### Why use use cases when many are small?

They create a clear domain vocabulary and keep ViewModels consistent. They also provide a natural location for action-specific rules such as trimming input. The tradeoff is more files, so in a smaller production feature I might use them only for nontrivial or reused actions.

## State Management

### Why expose immutable StateFlow?

The ViewModel should be the only state writer. Exposing `StateFlow` rather than `MutableStateFlow` prevents Composables or other consumers from mutating state and gives the UI a lifecycle-aware stream with a current value.

### Why not use LiveData?

StateFlow integrates naturally with coroutines and Flow-based Room/DataStore APIs. Compose collects it with `collectAsStateWithLifecycle`. LiveData would also work, but StateFlow keeps the asynchronous stack consistent.

### How do you model loading, error, empty, and success states?

Each feature has a UI state data class with loading flags, data, and an optional error message. The screen chooses loading, blocking error, empty, or content UI. The feed also supports a nonblocking cached-data error so a failed refresh does not hide cached posts.

### How are one-time events handled?

Durable state stays in StateFlow. One-time navigation and snackbar events use sealed effect types sent through a buffered Channel exposed as Flow. This prevents events from replaying just because Compose recomposed.

## Data Boundaries

### Why use the Repository Pattern?

Repositories provide a stable domain-facing API and hide where data comes from. For example, `FeedRepositoryImpl` coordinates fake network responses and Room cache, while `FeedViewModel` only depends on feed use cases. Tests can replace the repository with a fake.

### Why separate DTO, Entity, Domain, and UI models?

Each model changes for a different reason:

- DTO changes with the API contract.
- Entity changes with the database schema.
- Domain model changes with app rules.
- UI model changes with display needs.

Separating them prevents a database or API change from automatically leaking through every layer. The tradeoff is mapper code.

### Why is Room the source of truth for cached features?

Observing one local source simplifies UI state. Refresh writes into Room, Room emits Flow, and the screen updates. Cached data remains available after process restart or network failure. The implementation is intentionally simple and does not yet solve multi-device conflicts.

### Why use DataStore as well as Room?

DataStore is appropriate for small preferences and session fields; Room is better for relational data and queries. ConnectHub stores theme/language/session values in DataStore and posts/messages/notifications in Room.

## Coroutines And Flow

### When do you use suspend functions versus Flow?

Suspend functions represent a one-time action or result, such as login, refresh, or send message. Flow represents values that can change, such as posts, messages, notifications, session, and settings.

### How does debounced search work?

`SearchViewModel` holds the query in StateFlow, applies `debounce` and `distinctUntilChanged`, then uses `flatMapLatest` to cancel an older search when a newer query arrives. Recent searches are saved locally.

### How do you test coroutines?

JVM tests use Kotlin Coroutines Test and `MainDispatcherRule`. Tests advance the virtual scheduler and assert StateFlow values or effects. Search tests can advance virtual time to verify debounce behavior without waiting in real time.

## Dependency Injection

### How does Hilt help this project?

Hilt centralizes construction for the database, DAOs, DataStore, dispatchers, APIs, repositories, and ViewModels. Constructor injection makes dependencies explicit. Domain interfaces can be bound to fake/demo implementations without feature code knowing the implementation.

### Why inject dispatchers?

Repository code can use IO/default/main through an abstraction, and tests can provide a test dispatcher. This makes coroutine scheduling deterministic and avoids hard-coding dispatchers throughout business code.

## Navigation

### How does navigation work?

There is a root graph for splash/auth/main switching, a dedicated auth graph, and a main graph. Bottom navigation owns top-level social destinations, while a drawer exposes chats/settings/about/logout. Detail screens use route arguments and back navigation.

### How is login persistence connected to navigation?

The splash ViewModel observes the DataStore-backed auth session. The root NavHost selects the auth or main path. Logout clears the session and navigates back to the auth graph while removing the main graph from the back stack.

## Fake Backend And Migration Path

### Why use fake API interfaces instead of calling fake data directly from repositories?

The interface models a real remote boundary and keeps repository orchestration realistic. Delay/error simulation exercises loading and failure states. It also gives a clear replacement point for Retrofit.

### How would you replace the fake API with a real backend?

I would add Retrofit service implementations matching the existing interfaces or introduce remote data-source interfaces if HTTP details differ. Hilt bindings would select the real services. DTO mappers, repositories, domain models, ViewModels, and Composables could remain mostly unchanged. I would also add API error mapping, authentication refresh, retries, and integration tests.

### How would you support real offline writes?

I would add sync metadata such as pending/synced/failed status, local operation ids, timestamps or versions, and a WorkManager queue. Repository writes would update Room first, and a sync worker would reconcile with the backend using explicit conflict rules.

## Feature Questions

### How are bookmarks kept offline?

Bookmarks have a Room table and a transaction updates both the post flag and bookmark row. The bookmarks screen observes a DAO join, so removing a bookmark updates both feed and bookmark views through Flow.

### How does simulated chat reply work?

The send use case calls `ChatRepositoryImpl`. The repository persists the sent message, delays, creates a fake incoming message, and updates the conversation preview. The detail ViewModel exposes a typing flag while the suspend send operation is running.

### Are these Android system notifications?

No. They are in-app notification records fetched from a fake service and persisted in Room. WorkManager, FCM, runtime notification permission, and NotificationManager integration are future work.

### Does clear cache delete Room data?

Not currently. `SettingsRepositoryImpl.clearCache` records a cache-clear timestamp to keep the demo non-destructive. A production version should introduce a cache manager that clears selected tables while preserving session/settings as appropriate.

## Testing And Quality

### What test pyramid does the project use?

Most tests are fast JVM tests for ViewModels, use cases, and repositories. Instrumented tests cover Room DAOs and Compose rendering. Full end-to-end navigation tests are not yet present.

### What are the biggest remaining test gaps?

Room migration tests, navigation flows, feed interaction UI tests, repository failure paths, accessibility tests, and exact multi-emission Flow assertions.

## Scaling

### How would you modularize ConnectHub?

I would first extract stable shared modules such as `core:model`, `core:database`, `core:designsystem`, and `core:testing`. Then feature modules could own their routes, ViewModels, and UI. Domain interfaces should remain dependency-inversion boundaries. I would avoid modularization until build time or team ownership justifies it.

### What would you improve before production?

Real auth/security, encrypted tokens, Retrofit/OkHttp, robust error models, sync/conflict handling, paging, Room migration tests, accessibility, performance benchmarks, logging/monitoring, release CI, and secure configuration management.
