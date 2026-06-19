# Architecture

ConnectHub uses a clean MVVM-inspired architecture. It separates UI code, app rules, and data access without splitting a portfolio-sized app into unnecessary Gradle modules.

## Architecture At A Glance

```text
Compose UI
   | user action
   v
ViewModel
   | calls
   v
Use Case
   | depends on
   v
Domain Repository Interface
   | implemented by
   v
Data Repository
   | reads/writes
   v
Room / DataStore / Fake API
   | Flow or suspend result
   v
Repository -> ViewModel StateFlow -> Compose UI
```

The dependency direction matters: feature and domain code do not depend on Room entities or fake API DTOs. Data implementations depend on domain interfaces, and Hilt connects the pieces at runtime.

## Why MVVM

MVVM gives each feature a predictable shape:

- Compose renders state and sends user intent through callbacks.
- ViewModels coordinate screen behavior and survive configuration changes.
- Use cases give important actions a descriptive domain API.
- Repositories hide persistence and fake networking.

This makes screens easier to preview, test, and reason about. For example, `FeedScreen` does not know how Room or `FakeFeedApiService` works. It only receives `FeedUiState` and event callbacks.

## UI Layer

The UI layer lives under `feature/` and `core/designsystem/`.

Most features use two Compose entry points:

- A route Composable obtains a Hilt ViewModel and collects state with `collectAsStateWithLifecycle`.
- A screen/content Composable receives plain state and callbacks, which makes it easier to test.

Examples:

- `FeedRoute` and `FeedScreen`
- `SearchRoute` and `SearchScreen`
- `ProfileRoute` and `ProfileScreen`
- `ChatDetailRoute` and `ChatDetailScreen`

Reusable Material 3 components include buttons, text fields, avatars, top/bottom navigation, post cards, and loading/error/empty states.

## ViewModel Layer

ViewModels expose immutable `StateFlow`:

```kotlin
private val _uiState = MutableStateFlow(FeedUiState())
val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()
```

The UI calls functions such as:

- `FeedViewModel.refresh()`
- `FeedViewModel.likePost(postId)`
- `SearchViewModel.onQueryChange(query)`
- `ChatDetailViewModel.send()`
- `SettingsViewModel.updateTheme(enabled)`

ViewModels use `viewModelScope` for coroutine work and convert domain models into UI models where presentation formatting is useful.

## Domain And Use Case Layer

The domain layer contains:

- Models such as `User`, `Post`, `Comment`, `Conversation`, `Message`, `AppNotification`, and `AppSettings`
- Repository interfaces such as `FeedRepository`, `ChatRepository`, and `SettingsRepository`
- Use cases such as `ObserveFeedUseCase`, `CreatePostUseCase`, `SendMessageUseCase`, and `UpdateThemeUseCase`

Use cases are intentionally small. They make important behavior explicit, keep ViewModel constructors readable, and provide a stable place for validation or orchestration rules.

## Repository Layer

Repository interfaces live in `domain/repository`; implementations live in `data/repository`.

Repositories are the boundary between app logic and data sources. They:

- Call fake API interfaces
- Map DTOs into Room entities
- Preserve local state such as bookmarks and notification read status
- Expose Room-backed Flow as domain models
- Read and write DataStore preferences

Examples:

- `FeedRepositoryImpl` refreshes remote posts and exposes cached posts.
- `CommentRepositoryImpl` coordinates fake comments and the comment DAO.
- `ChatRepositoryImpl` caches conversations/messages and creates a delayed fake reply.
- `SettingsRepositoryImpl` persists app preferences through DataStore.

## Data Layer And Model Boundaries

ConnectHub uses different model types for different jobs:

| Model | Purpose | Example |
| --- | --- | --- |
| DTO | Fake remote response/request | `PostDto`, `MessageDto` |
| Entity | Room table row | `PostEntity`, `MessageEntity` |
| Domain | App/business meaning | `Post`, `Message` |
| UI model | Screen-ready presentation | `PostUiModel`, `MessageUiModel` |

Mapper functions keep conversions explicit. This prevents Room annotations and fake API details from leaking into domain or UI code.

## Room Database

`ConnectHubDatabase` is the local cache and currently uses schema version 4.

Tables:

- `posts`
- `bookmarked_posts`
- `comments`
- `recent_searches`
- `profiles`
- `conversations`
- `messages`
- `notifications`

DAOs return Flow where live updates are useful. Database migrations document schema growth from comments in version 2, profile/search in version 3, and chat/notifications in version 4.

## DataStore

Preferences DataStore stores small key-value state that does not belong in relational tables.

`SessionLocalDataSource` stores:

- Fake auth token
- Current user id and profile snapshot
- First-launch value

`SettingsLocalDataSource` stores:

- Dark mode
- Dynamic color
- Notification preference
- Language
- Last cache-clear timestamp

The app theme observes settings through `AppSettingsViewModel`, so dark mode and dynamic color changes are applied at the activity level.

## Fake Network Layer

Retrofit-style interfaces define the remote contract even though no HTTP client is used:

- `AuthApiService`
- `FeedApiService`
- `CommentApiService`
- `ChatApiService`
- `NotificationApiService`

Fake implementations return hard-coded DTOs after coroutine `delay`. `FakeNetworkConfig.errorSimulationEnabled` can enable failure paths. Replacing a fake service with Retrofit would mainly affect the DI/data implementation, not ViewModels or Composables.

## Offline-First Data Flow

Room is the observable source of truth for cached feature data.

Feed refresh example:

```text
FeedScreen pull-to-refresh
  -> FeedViewModel.refresh
  -> RefreshFeedUseCase
  -> FeedRepositoryImpl.refreshFeed
  -> FakeFeedApiService.getFeed
  -> PostDto mapped to PostEntity
  -> PostDao upsert
  -> PostDao Flow emits
  -> FeedRepository maps PostEntity to Post
  -> FeedViewModel maps Post to PostUiModel
  -> FeedScreen recomposes
```

If refresh fails but Room already contains posts, the feed can continue showing cached content with an error message.

## Navigation

Navigation Compose uses three levels:

- Root: splash, auth graph, and main graph
- Auth: login and sign up
- Main: feed, search, bookmarks, notifications, profile, chats, settings, about, and detail destinations

The bottom bar contains feed, search, bookmarks, notifications, and profile. The drawer contains feed, chats, settings, about, and logout. Detail destinations use route arguments and do not appear in bottom navigation.

## One-Time Effects

StateFlow represents durable screen state. One-time work such as navigation and snackbars uses sealed effects delivered through a buffered `Channel` exposed as Flow.

Examples:

- `LoginEffect.NavigateToHome`
- `CreatePostEffect.NavigateBack`
- `EditProfileEffect.NavigateBack`
- `SettingsEffect.ShowSnackbar`

This avoids storing one-time navigation commands in persistent UI state.

## Loading, Error, Empty, And Success States

Feature state objects include fields such as `isLoading`, `isRefreshing`, content lists, and `errorMessage`. Shared components render loading, error, and empty states consistently.

The feed also distinguishes a blocking error with no cached data from a refresh error where cached posts remain usable.

## Dependency Injection

Hilt modules provide:

- DataStore
- Room database and DAOs
- Coroutine dispatcher abstraction
- Fake API interface implementations
- Repository interface implementations

Constructor injection keeps dependencies visible and allows tests to construct ViewModels/use cases with fakes.

## Scaling The Architecture

The package boundaries are suitable for the current single-module app. If the project grows, packages can become Gradle modules such as `core:database`, `core:designsystem`, and `feature:feed` while preserving the same domain interfaces and data flow.
