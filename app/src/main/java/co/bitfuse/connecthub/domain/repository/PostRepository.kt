package co.bitfuse.connecthub.domain.repository

import co.bitfuse.connecthub.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun observePost(postId: String): Flow<Post?>
    suspend fun refreshPost(postId: String)
    suspend fun createPost(content: String, imageUrl: String?): Post
}
