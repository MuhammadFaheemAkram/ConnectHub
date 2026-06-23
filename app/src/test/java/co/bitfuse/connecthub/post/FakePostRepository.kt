package co.bitfuse.connecthub.post

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.PostRepository
import co.bitfuse.connecthub.feed.FeedTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePostRepository(
    initialPost: Post? = FeedTestData.post(id = "post-1"),
) : PostRepository {
    private val post = MutableStateFlow(initialPost)
    var shouldThrow = false
    var createdContent: String? = null

    override fun observePost(postId: String): Flow<Post?> = post

    override suspend fun refreshPost(postId: String) {
        if (shouldThrow) error("Post refresh failed")
        if (post.value == null) post.value = FeedTestData.post(id = postId)
    }

    override suspend fun createPost(content: String, imageUrl: String?): Post {
        if (shouldThrow) error("Create failed")
        createdContent = content
        return FeedTestData.post(id = "created-post").copy(content = content, imageUrl = imageUrl).also {
            post.value = it
        }
    }
}
