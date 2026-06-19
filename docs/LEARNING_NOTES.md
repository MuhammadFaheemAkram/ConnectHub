# Learning Notes

This guide turns ConnectHub into a study map for beginner-to-intermediate Android developers. Every concept below is demonstrated in the current repository.

## 1. MVVM

MVVM separates a screen into presentation and state-management responsibilities.

In ConnectHub:

- Compose functions render `UiState` and invoke callbacks.
- ViewModels own `MutableStateFlow`, call use cases, and expose immutable `StateFlow`.
- Repositories and use cases keep data access outside ViewModels.

Example path:

```text
FeedScreen
  -> onLikePost(postId)
  -> FeedViewModel.likePost(postId)
  -> LikePostUseCase
  -> FeedRepository
```

Learning point: a ViewModel should know what the user wants to do, but not how a SQL query or HTTP request is implemented.

## 2. Jetpack Compose

ConnectHub uses Compose for every screen and reusable UI component.

Concepts demonstrated:

- `@Composable` functions
- Material 3 `Scaffold`, app bars, navigation bars, drawers, cards, chips, switches, and text fields
- `LazyColumn` and `LazyRow`
- Stateless screen content functions
- `LaunchedEffect` for collecting one-time UI effects or loading route arguments
- Lifecycle-aware StateFlow collection
- Compose UI tests

The route/screen split is especially useful:

```text
FeedRoute: obtains ViewModel and collects StateFlow
FeedScreen: receives FeedUiState and callbacks
```

Learning point: state hoisting makes UI easier to preview and test.

## 3. StateFlow

StateFlow represents the latest durable state of a screen.

ViewModels follow this pattern:

```kotlin
private val _uiState = MutableStateFlow(SettingsUiState())
val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
```

Why expose the immutable type:

- Only the ViewModel can mutate screen state.
- Composables receive a stable observable contract.
- Tests can read the latest state through `uiState.value`.

State includes more than successful data. It also includes loading, refreshing, empty, validation, submission, typing, and error conditions.

## 4. Kotlin Coroutines And Flow

Coroutines are used for fake network delay, database writes, DataStore writes, and ViewModel work.

Flow is used for observable data:

- Room DAO queries
- Auth session
- App settings
- Feed posts
- Comments
- Bookmarks
- Search history
- Profile
- Conversations and messages
- Notifications

Useful operators demonstrated include `map`, `catch`, `debounce`, `distinctUntilChanged`, `flatMapLatest`, `filterNotNull`, `stateIn`, and `update`.

Learning point: use suspend functions for one result/action and Flow for values that can change over time.

## 5. Repository Pattern

Repository interfaces belong to the domain layer. Implementations belong to the data layer.

For example:

```text
domain/repository/FeedRepository.kt
data/repository/FeedRepositoryImpl.kt
```

Benefits:

- ViewModels do not depend on Room or fake API classes.
- Tests replace repositories with fakes.
- A real backend can replace fake services without rewriting UI logic.
- Local and remote data can be coordinated in one place.

## 6. Use Cases

Use cases represent meaningful actions such as:

- Login
- Observe session
- Refresh feed
- Like or bookmark post
- Create post
- Add comment
- Search
- Send message
- Mark notification read
- Update theme

Most are intentionally thin. Their value is a clear domain vocabulary and a stable place for action-specific validation or orchestration.

Learning point: not every getter needs a use case in every app, but consistent use cases can make a learning project easier to navigate and test.

## 7. Hilt Dependency Injection

Hilt creates and connects dependencies.

The modules provide or bind:

- `DataStore<Preferences>`
- `ConnectHubDatabase`
- DAOs
- Dispatcher abstraction
- Fake API implementations
- Repository implementations

`@HiltViewModel` classes use constructor injection. This avoids manual service locators and makes each class's dependencies visible.

Learning point: dependency injection is most valuable at boundaries where implementations vary, such as API services, repositories, databases, and dispatchers.

## 8. Room

Room provides structured offline storage.

ConnectHub demonstrates:

- `@Entity` table models
- `@Dao` interfaces
- `@Query` and `@Insert`
- Flow-returning queries
- Foreign keys for comments/messages
- Transactions for bookmark state
- Database migrations
- In-memory DAO tests

Room stores posts, bookmarks, comments, recent searches, profiles, conversations, messages, and notifications.

Learning point: entities are persistence models. They should not be passed directly through the whole app because database concerns can then leak into UI and domain logic.

## 9. DataStore

Preferences DataStore stores small asynchronous key-value data.

ConnectHub uses it for:

- Fake auth token and current user snapshot
- First-launch value
- Dark mode
- Dynamic color
- Notification preference
- Language

DataStore exposes Flow, so the app theme can react to preference changes without manual callbacks.

Learning point: use DataStore for preferences and Room for relational/queryable records.

## 10. Navigation Compose

The app uses route definitions and separate root/auth/main navigation areas.

Concepts demonstrated:

- Nested graph structure
- Route constants through sealed classes
- String route arguments for post, comments, and chat detail
- Bottom navigation with state restoration
- Modal navigation drawer
- Back navigation for detail screens
- Root graph switching after session check and logout

Learning point: keep navigation calls in route-level Composables or graph builders, not reusable content Composables.

## 11. One-Time Effects

StateFlow should describe what is true now. Navigation and snackbars are events that should happen once.

ConnectHub uses sealed effect interfaces and buffered channels for flows such as:

- Login success navigation
- Sign-up success navigation
- Create-post back navigation
- Edit-profile back navigation
- Settings snackbar

Learning point: storing a navigation event as a Boolean in durable state can cause it to fire again after recomposition or recreation.

## 12. Offline-First Design

The app generally observes Room and treats fake API calls as refresh operations.

Advantages:

- Cached content can appear immediately.
- UI does not switch between unrelated remote/local model sources.
- A failed refresh does not have to erase usable content.
- Bookmark/read state stays local and observable.

This is a simplified offline-first implementation. It does not yet include conflict metadata, background sync, or a durable write queue.

## 13. Fake APIs

Fake services implement Retrofit-style interfaces and use coroutine delay.

This demonstrates API boundaries without requiring backend deployment. It also keeps repository code shaped like production data coordination code.

Learning point: an interface is useful when it separates a meaningful boundary. Here, it allows fake implementations now and Retrofit implementations later.

## 14. Testing

ConnectHub demonstrates several testing levels:

- Pure validation/use-case tests
- ViewModel coroutine tests
- Repository tests with fakes
- DataStore repository test with a temporary file
- Room DAO tests with an in-memory database
- Stateless Compose UI tests

`MainDispatcherRule` is important because ViewModels use `viewModelScope`, which expects a main dispatcher.

## 15. Notifications And Background Work

The app has an in-app notification feed with Room persistence and fake API refresh. It does not currently implement Android system notifications, Firebase Cloud Messaging, or WorkManager.

Learning point: an in-app notification model and an Android system notification are different concerns. The current project demonstrates the former.

## Suggested Study Order

1. Start with `domain/model` and one repository interface.
2. Follow a feed action from screen to ViewModel to use case to repository.
3. Compare `PostDto`, `PostEntity`, `Post`, and `PostUiModel`.
4. Read `PostDao` and its instrumented tests.
5. Read auth/session DataStore flow.
6. Trace root navigation after login/logout.
7. Study fake repository ViewModel tests.
8. Trace settings from DataStore to `MainActivity` theme.
