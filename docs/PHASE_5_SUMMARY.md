# Phase 5 Summary: Search, Bookmarks, And Profile

## Phase Goal

Add discovery and personal content features using the existing cached post data, plus local persistence for recent searches and profile edits.

## What Was Implemented

- Debounced post/people search
- Recent search Room entity/DAO and clear/select behavior
- Offline bookmarks screen observing cached bookmarked posts
- Profile Room entity/DAO/repository/use cases
- Profile screen with user stats and authored posts
- Edit-profile validation and persistence
- Logout action from profile
- Room migration from version 2 to 3
- Unit tests for search, bookmarks, profile, edit profile, and use cases
- Room tests for recent searches, profile, post search, and author filtering

## Android Concepts Demonstrated

- Debounce with Flow
- `flatMapLatest` cancellation
- SQL `LIKE` search
- Derived UI state
- Repository reuse of existing post cache
- Room upsert for profile settings
- Form validation and one-time effects
- Database migration

## Data Flow

Search:

```text
Search field text
  -> SearchViewModel query StateFlow
  -> debounce + distinctUntilChanged
  -> SaveRecentSearchUseCase
  -> SearchUseCase
  -> SearchRepositoryImpl
  -> PostDao.searchPosts + in-memory sample users
  -> SearchResults
  -> SearchUiState
```

Edit profile:

```text
EditProfileScreen save
  -> EditProfileViewModel validates
  -> UpdateProfileUseCase
  -> ProfileRepositoryImpl
  -> ProfileDao.upsertProfile
  -> profile Flow emits
  -> profile screen updates
```

## Important Files

- `data/search/RecentSearchEntity.kt`: recent query table.
- `data/search/RecentSearchDao.kt`: observe/upsert/clear recent searches.
- `data/repository/SearchRepositoryImpl.kt`: cached post and sample-user search.
- `feature/search/SearchViewModel.kt`: debounce pipeline and UI state.
- `data/repository/BookmarkRepositoryImpl.kt`: offline bookmark observation/removal.
- `feature/bookmarks/BookmarksViewModel.kt`: bookmark screen state.
- `data/profile/ProfileEntity.kt`: persisted profile row.
- `data/profile/ProfileDao.kt`: profile read/upsert API.
- `data/repository/ProfileRepositoryImpl.kt`: default profile and authored posts.
- `feature/profile/ProfileViewModel.kt`: profile/posts state.
- `feature/profile/EditProfileViewModel.kt`: initial form state, validation, save effect.

## Important Classes

### `SearchViewModel`

Uses a query StateFlow with debounce and `flatMapLatest`, ensuring old searches do not win after a newer query is entered.

### `BookmarkRepositoryImpl`

Exposes the Room join as domain posts and removes bookmark state through the DAO transaction.

### `ProfileRepositoryImpl`

Ensures a default profile exists, exposes profile/authored-post Flow, and persists edited fields.

## Interview Notes

### Why use `flatMapLatest` for search?

It cancels collection of the previous search Flow when a new query arrives, avoiding stale result races.

### Is user search fully offline?

Post search is Room-backed; people search uses a hard-coded in-memory list in the fake repository. A production version would use a user table or backend search.

### Why is bookmarks a separate repository if feed can bookmark?

It gives the bookmarks feature a focused read/remove contract while the feed repository owns remote mutation behavior. Both use the same Room tables.

## Learning Checklist

- [ ] I can explain debounce and `flatMapLatest`.
- [ ] I can find the SQL post search query.
- [ ] I understand how recent searches are ordered and limited.
- [ ] I can trace bookmark removal into both screens.
- [ ] I can trace profile edits through Room Flow.
- [ ] I can find the version 2-to-3 migration.

## Future Improvements

- Store/search users in Room or use backend search.
- Add full-text search for larger datasets.
- Add recent-search deletion per item.
- Display remote avatars in the shared avatar component.
- Connect profile identity to the actual signed-in user rather than the seeded demo id.
