# Phase 3 Summary: Feed And Offline Cache

## Phase Goal

Build the central social feed with fake paged data, Room caching, pull to refresh, likes, bookmarks, and offline-friendly state handling.

## What Was Implemented

- `PostDto`, `PostEntity`, `Post`, and `PostUiModel`
- Fake feed API with seeded posts, delay, pagination, like, bookmark, details, and create-post endpoints
- Room posts and bookmarked-posts tables
- `PostDao` Flow queries and bookmark transaction
- Feed repository and feed use cases
- `FeedViewModel` with initial load, refresh, load-more, like, and bookmark intents
- Pull-to-refresh feed UI and reusable post cards
- Coil-backed optional post images
- Loading, empty, blocking error, and cached-data error states
- Unit tests for use cases, repository, and ViewModel
- Room tests for posts and bookmarks

## Android Concepts Demonstrated

- Offline-first repository coordination
- Room entities, DAO queries, transactions, and Flow
- DTO/entity/domain/UI model mapping
- Optimistic-looking UI through observable cache updates
- Pull-to-refresh Compose API
- Simple page-based pagination
- Coil image loading
- Coroutine error handling

## Data Flow

Observe and refresh:

```text
FeedRoute collects FeedViewModel.uiState
  <- FeedViewModel collects ObserveFeedUseCase
  <- FeedRepositoryImpl observes PostDao.observeFeed
  <- Room emits PostEntity rows

Pull to refresh
  -> RefreshFeedUseCase
  -> FakeFeedApiService.getFeed(page)
  -> PostDto mapped to PostEntity
  -> PostDao.upsertPosts
  -> Room Flow emits updated feed
```

Bookmark:

```text
Post card bookmark icon
  -> FeedViewModel.bookmarkPost
  -> BookmarkPostUseCase
  -> FeedRepositoryImpl
  -> fake API mutation + PostDao.setBookmarkState
  -> feed/bookmark queries emit updated state
```

## Important Files

- `data/feed/FeedApiService.kt`: feed remote contract.
- `data/feed/FakeFeedApiService.kt`: seeded, paged fake feed.
- `data/feed/PostEntity.kt`: cached post table model.
- `data/feed/BookmarkedPostEntity.kt`: bookmark relation table.
- `data/feed/PostDao.kt`: feed/search/bookmark queries and transaction.
- `data/mapper/PostMappers.kt`: DTO/entity/domain mapping.
- `data/repository/FeedRepositoryImpl.kt`: remote/local coordination.
- `feature/feed/FeedViewModel.kt`: feed intents and state.
- `feature/feed/FeedScreen.kt`: pull-to-refresh and feed rendering.
- `core/designsystem/component/ConnectHubPostCard.kt`: reusable post UI.

## Important Classes

### `FeedRepositoryImpl`

Preserves bookmarked ids during refresh, maps remote data to entities, and exposes Room-backed domain Flow.

### `PostDao`

Provides observable feed/bookmark queries and atomically updates bookmark table plus post bookmark flag.

### `FeedUiState`

Models loading, refreshing, loading more, posts, errors, and whether another page is available.

## Interview Notes

### Why is Room the source of truth?

It provides one observable data source for cached and refreshed content. The UI does not switch between separate remote and local lists.

### Is this Paging 3?

No. It is explicit page loading suitable for the small fake dataset. Paging 3 would be appropriate for larger datasets and more complete load-state handling.

### How are bookmarks preserved during refresh?

The repository reads local bookmarked ids and uses them as an override while mapping refreshed DTOs, preventing the fake remote snapshot from erasing local bookmark state.

## Learning Checklist

- [ ] I can compare DTO, entity, domain, and UI post models.
- [ ] I can trace refresh from API to Room to UI.
- [ ] I understand Flow-returning DAO queries.
- [ ] I can explain the bookmark transaction.
- [ ] I understand blocking versus cached-data errors.
- [ ] I know why the current pagination is not Paging 3.

## Future Improvements

- Adopt Paging 3 if the feed grows.
- Export Room schemas and test migrations.
- Add retry policy and structured data errors.
- Add Compose interaction tests for like/bookmark actions.
- Add sync metadata for durable offline mutations.
