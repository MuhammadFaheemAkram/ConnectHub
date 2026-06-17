# API Simulation

ConnectHub uses Retrofit-style interfaces with fake implementations. This keeps the data layer realistic without requiring a backend.

## Design

Each fake API exposes suspend functions and lives behind an interface:

```kotlin
interface FeedApiService {
    suspend fun getFeed(page: Int): List<PostDto>
    suspend fun getPostDetails(postId: String): PostDto
    suspend fun likePost(postId: String): PostDto
    suspend fun bookmarkPost(postId: String): PostDto
    suspend fun createPost(request: CreatePostRequestDto): PostDto
}
```

Fake implementations:

- Return hard-coded DTOs
- Simulate network delay with `delay`
- Mutate in-memory fake data for actions like like, bookmark, create post, and send message
- Can simulate errors through `FakeNetworkConfig.errorSimulationEnabled`

## Services

### Auth

`AuthApiService`

- `login(email, password)`
- `signUp(name, email, password)`

Returns an auth token, user id, and user profile DTO.

### Feed

`FeedApiService`

- `getFeed(page)`
- `getPostDetails(postId)`
- `likePost(postId)`
- `bookmarkPost(postId)`
- `createPost(request)`

Supports simple pagination and local mutation of fake posts.

### Comments

`CommentApiService`

- `getComments(postId)`
- `addComment(postId, text)`

Provides seeded comments and creates new own-comment DTOs.

### Chat

`ChatApiService`

- `getConversations()`
- `getMessages(conversationId)`
- `sendMessage(conversationId, text)`

The repository adds a delayed simulated reply after sending a message.

### Notifications

`NotificationApiService`

- `getNotifications()`

Returns Like, Comment, Follow, and Mention notification DTOs.

## Error Simulation

`FakeNetworkConfig` controls disabled-by-default fake failures:

```kotlin
object FakeNetworkConfig {
    const val defaultDelayMillis = 450L
    const val errorSimulationEnabled = false
}
```

Turn the flag on locally to exercise cached-data and error UI paths.

## Offline Cache Interaction

Repositories call fake APIs, map DTOs to Room entities, and write them to local tables. Screens observe Room-backed Flow and render cached data even when refresh work fails.
