package co.bitfuse.connecthub.feed

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFeedRepository(
    initialPosts: List<Post> = emptyList(),
) : FeedRepository {
    private val posts = MutableStateFlow(initialPosts)
    var shouldThrowOnRefresh = false
    var likedPostId: String? = null
    var bookmarkedPostId: String? = null

    override fun observeFeed(): Flow<List<Post>> = posts

    override suspend fun refreshFeed(page: Int): Boolean {
        if (shouldThrowOnRefresh) error("Refresh failed")
        posts.value = listOf(
            FeedTestData.post(id = "post-$page"),
        )
        return page < 2
    }

    override suspend fun likePost(postId: String) {
        likedPostId = postId
        posts.value = posts.value.map { post ->
            if (post.id == postId) {
                val liked = !post.isLiked
                post.copy(
                    isLiked = liked,
                    likeCount = (post.likeCount + if (liked) 1 else -1).coerceAtLeast(0),
                )
            } else {
                post
            }
        }
    }

    override suspend fun bookmarkPost(postId: String) {
        bookmarkedPostId = postId
        posts.value = posts.value.map { post ->
            if (post.id == postId) post.copy(isBookmarked = !post.isBookmarked) else post
        }
    }
}
