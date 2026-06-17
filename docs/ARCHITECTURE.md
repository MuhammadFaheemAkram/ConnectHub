# Architecture

ConnectHub uses a clean MVVM-inspired architecture that keeps UI, domain logic, persistence, and fake networking separate while staying understandable for a portfolio project.

## Why MVVM

MVVM gives each screen a predictable structure:

- Compose renders immutable UI state.
- ViewModels own state and user intent handling.
- Use cases describe important actions.
- Repositories hide data sources.

This keeps Composables easy to preview and test, and it keeps data work out of the UI layer.

## Data Flow

```text
User action
  -> Compose callback
  -> ViewModel function
  -> UseCase
  -> Domain Repository interface
  -> Data Repository implementation
  -> Fake API / Room / DataStore
  -> Flow or suspend result
  -> StateFlow UI state
  -> Compose recomposition
```

Example: liking a post calls `FeedViewModel.likePost(postId)`, which delegates to `LikePostUseCase`, which calls `FeedRepository`, which updates fake API state and Room cache. The feed UI observes Room-backed Flow and updates through `StateFlow`.

## Models

ConnectHub uses separate model types for separate responsibilities:

- DTOs: fake remote models returned by fake API services.
- Entities: Room table models.
- Domain models: app logic models used by use cases and repository interfaces.
- UI models: screen-specific display models when the UI benefits from preformatted values.

Mapper functions live in `data/mapper` and feature mappers, keeping conversion code explicit and easy to audit.

## StateFlow

ViewModels expose immutable `StateFlow`:

```kotlin
private val _uiState = MutableStateFlow(FeedUiState())
val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()
```

Compose screens collect state with `collectAsStateWithLifecycle`. UI state includes loading, empty, success, and error information so screens can render all important states.

## One-Time Effects

Navigation and snackbars use sealed effect types where needed. For example, create-post and edit-profile flows emit one-time navigation effects through a `Channel` exposed as a Flow.

## Offline-First Approach

Room is the source of truth for feed posts, bookmarks, comments, recent searches, profile data, conversations, messages, and notifications. Refresh functions call fake APIs and write into Room. Screens observe Room-backed Flow, so cached data is available immediately and remains visible if refresh fails.

DataStore stores session and app settings:

- Auth token and current user metadata
- Dark mode
- Dynamic color
- Notification setting
- Language setting

## Dependency Injection

Hilt wires fake API services, Room DAOs, repositories, DataStore, and dispatchers. Domain code depends on interfaces rather than data implementations, making ViewModel and use-case tests simple.

## Navigation

Navigation Compose defines:

- Root graph: splash, auth graph, main graph
- Auth graph: login and sign up
- Main graph: feed, search, bookmarks, notifications, profile, chats, settings, details, comments, create post, edit profile, and about
- Bottom navigation: feed, search, bookmarks, notifications, profile
- Drawer navigation: feed, chats, settings, about, logout

Detail screens are intentionally excluded from bottom navigation.
