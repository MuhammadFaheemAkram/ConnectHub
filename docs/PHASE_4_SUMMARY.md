# Phase 4 Summary: Post Detail, Comments, And Create Post

## Phase Goal

Expand the feed into complete post interactions: detailed viewing, cached comments, adding/deleting comments, and creating a post that immediately enters the local cache.

## What Was Implemented

- Post detail repository/use cases/ViewModel/screen
- Comment DTO, entity, domain model, DAO, fake API, repository, use cases, UI model, and screens
- Comment preview on post detail
- Add comment from detail and full comments screen
- Delete-own-comment behavior
- Create-post validation, image URL input, character counter, fake submission, and cache insert
- Room migration from version 1 to 2 for comments
- Unit tests for post detail, comments, create post, and use cases
- Room comment DAO tests
- Compose tests for post detail and create post

## Android Concepts Demonstrated

- Route arguments and `LaunchedEffect` loading
- One-time navigation/snackbar effects
- Room foreign keys and indexed child columns
- Validation state in ViewModels
- Multiple repositories coordinated in one detail ViewModel
- Stateless Compose content testing
- Database migration

## Data Flow

Post detail:

```text
Post detail route argument
  -> PostDetailViewModel.load(postId)
  -> observe cached post and comment Flow
  -> refresh post/comments through fake APIs
  -> repositories write Room
  -> StateFlow emits post plus comment preview
```

Create post:

```text
CreatePostScreen submit
  -> CreatePostViewModel validates
  -> CreatePostUseCase
  -> PostRepositoryImpl.createPost
  -> FakeFeedApiService.createPost
  -> PostDto mapped to PostEntity
  -> PostDao.upsertPost
  -> CreatePostEffect.NavigateBack
  -> feed Room Flow includes new post
```

## Important Files

- `data/comment/CommentApiService.kt`: comment remote contract.
- `data/comment/FakeCommentApiService.kt`: seeded comments and add-comment behavior.
- `data/comment/CommentEntity.kt`: Room comment row with post foreign key.
- `data/comment/CommentDao.kt`: observe/upsert/delete-own queries.
- `data/repository/CommentRepositoryImpl.kt`: fake API and Room coordination.
- `data/repository/PostRepositoryImpl.kt`: detail refresh and create-post behavior.
- `feature/postdetail/PostDetailViewModel.kt`: post plus comment preview state.
- `feature/comments/CommentsViewModel.kt`: list/add/delete intents.
- `feature/createpost/CreatePostViewModel.kt`: validation, submission, and effects.
- `di/DatabaseModule.kt`: version 1-to-2 migration.

## Important Classes

### `PostDetailViewModel`

Combines post observation and comment observation while exposing like, bookmark, refresh, and add-comment actions.

### `CommentsViewModel`

Loads a post-specific comments Flow, validates comment drafts, and permits deletion through repository rules.

### `CreatePostEffect`

Separates one-time navigation/snackbar work from persistent form state.

## Interview Notes

### Why use a foreign key for comments?

It expresses that a comment belongs to a post and allows cascade cleanup when a post row is deleted.

### Why does post detail observe local data and also refresh?

Observation provides immediate cached content; refresh updates the cache. This keeps one source of truth for the UI.

### Why test a stateless content Composable?

It avoids needing Hilt/navigation in a rendering test and verifies the UI contract with explicit state and callbacks.

## Learning Checklist

- [ ] I can trace a post id route argument into a ViewModel.
- [ ] I understand the comment foreign key.
- [ ] I can explain comment preview versus full comments UI.
- [ ] I can trace create post into the feed cache.
- [ ] I understand why navigation is an effect.
- [ ] I can find the first Room migration.

## Future Improvements

- Add comment pagination and optimistic pending state.
- Add edit-comment behavior.
- Add image picker/upload rather than URL input.
- Add migration tests using exported schemas.
- Add richer Compose interaction/navigation tests.
