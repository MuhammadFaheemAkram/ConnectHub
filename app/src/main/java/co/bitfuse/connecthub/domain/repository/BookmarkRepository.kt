package co.bitfuse.connecthub.domain.repository

import co.bitfuse.connecthub.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun observeBookmarks(): Flow<List<Post>>
    suspend fun removeBookmark(postId: String)
}
