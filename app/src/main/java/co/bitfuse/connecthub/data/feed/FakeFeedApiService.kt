package co.bitfuse.connecthub.data.feed

import co.bitfuse.connecthub.core.network.FakeNetworkConfig
import co.bitfuse.connecthub.data.auth.UserDto
import javax.inject.Inject
import kotlinx.coroutines.delay

class FakeFeedApiService @Inject constructor() : FeedApiService {
    private val pageSize = 3
    private val posts = seedPosts().toMutableList()

    override suspend fun getFeed(page: Int): List<PostDto> {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()
        val fromIndex = ((page - 1).coerceAtLeast(0)) * pageSize
        return posts.drop(fromIndex).take(pageSize)
    }

    override suspend fun getPostDetails(postId: String): PostDto {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()
        return posts.first { it.id == postId }
    }

    override suspend fun likePost(postId: String): PostDto {
        delay(FakeNetworkConfig.defaultDelayMillis / 2)
        maybeThrowSimulatedError()
        return updatePost(postId) { post ->
            val liked = !post.isLiked
            post.copy(
                isLiked = liked,
                likeCount = (post.likeCount + if (liked) 1 else -1).coerceAtLeast(0),
            )
        }
    }

    override suspend fun bookmarkPost(postId: String): PostDto {
        delay(FakeNetworkConfig.defaultDelayMillis / 2)
        maybeThrowSimulatedError()
        return updatePost(postId) { post ->
            post.copy(isBookmarked = !post.isBookmarked)
        }
    }

    override suspend fun createPost(request: CreatePostRequestDto): PostDto {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()
        val post = PostDto(
            id = "post-${System.currentTimeMillis()}",
            author = alex,
            content = request.content,
            imageUrl = request.imageUrl,
            createdAt = System.currentTimeMillis(),
            likeCount = 0,
            commentCount = 0,
            isLiked = false,
            isBookmarked = false,
        )
        posts.add(index = 0, element = post)
        return post
    }

    private fun updatePost(
        postId: String,
        update: (PostDto) -> PostDto,
    ): PostDto {
        val index = posts.indexOfFirst { it.id == postId }
        check(index >= 0) { "Post not found" }
        val updated = update(posts[index])
        posts[index] = updated
        return updated
    }

    private fun maybeThrowSimulatedError() {
        if (FakeNetworkConfig.errorSimulationEnabled) {
            error("Fake feed service is simulating a network failure.")
        }
    }

    companion object {
        private val maya = UserDto(
            id = "user-maya",
            name = "Maya Chen",
            email = "maya@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
            bio = "Android Engineer",
            followersCount = 8200,
            followingCount = 312,
        )
        private val omar = UserDto(
            id = "user-omar",
            name = "Omar Malik",
            email = "omar@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
            bio = "Product Designer",
            followersCount = 5100,
            followingCount = 440,
        )
        private val nadia = UserDto(
            id = "user-nadia",
            name = "Nadia Khan",
            email = "nadia@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
            bio = "Engineering Manager",
            followersCount = 12700,
            followingCount = 540,
        )
        private val alex = UserDto(
            id = "user-alex",
            name = "Alex Morgan",
            email = "alex@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9",
            bio = "Mobile Lead",
            followersCount = 1240,
            followingCount = 318,
        )

        private fun seedPosts(): List<PostDto> {
            val now = System.currentTimeMillis()
            return listOf(
                PostDto(
                    id = "post-001",
                    author = maya,
                    content = "Shipping small, well-tested slices keeps product work calm. Today I am sketching the offline feed cache before the API surface gets locked.",
                    imageUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3",
                    createdAt = now - 12 * 60_000,
                    likeCount = 42,
                    commentCount = 8,
                    isLiked = true,
                    isBookmarked = false,
                ),
                PostDto(
                    id = "post-002",
                    author = omar,
                    content = "A good social feed needs rhythm: readable cards, quick actions, and just enough visual density to keep scanning effortless.",
                    imageUrl = null,
                    createdAt = now - 60 * 60_000,
                    likeCount = 28,
                    commentCount = 4,
                    isLiked = false,
                    isBookmarked = true,
                ),
                PostDto(
                    id = "post-003",
                    author = nadia,
                    content = "Offline-first is not just resilience. It changes how fast the product feels because the UI can trust local data first.",
                    imageUrl = null,
                    createdAt = now - 3 * 60 * 60_000,
                    likeCount = 73,
                    commentCount = 11,
                    isLiked = false,
                    isBookmarked = false,
                ),
                PostDto(
                    id = "post-004",
                    author = alex,
                    content = "Clean architecture works best when it protects change without hiding simple ideas behind ceremony.",
                    imageUrl = "https://images.unsplash.com/photo-1497366811353-6870744d04b2",
                    createdAt = now - 8 * 60 * 60_000,
                    likeCount = 19,
                    commentCount = 3,
                    isLiked = false,
                    isBookmarked = false,
                ),
                PostDto(
                    id = "post-005",
                    author = maya,
                    content = "A ViewModel should feel almost boring: immutable state out, clear intent functions in, no UI toolkit leakage.",
                    imageUrl = null,
                    createdAt = now - 24 * 60 * 60_000,
                    likeCount = 56,
                    commentCount = 9,
                    isLiked = false,
                    isBookmarked = false,
                ),
                PostDto(
                    id = "post-006",
                    author = omar,
                    content = "Placeholder states are part of the product. Empty, loading, and error screens deserve the same care as the happy path.",
                    imageUrl = null,
                    createdAt = now - 2 * 24 * 60 * 60_000,
                    likeCount = 35,
                    commentCount = 6,
                    isLiked = true,
                    isBookmarked = false,
                ),
            )
        }
    }
}
