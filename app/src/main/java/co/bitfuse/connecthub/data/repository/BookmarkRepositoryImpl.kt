package co.bitfuse.connecthub.data.repository

import co.bitfuse.connecthub.core.common.dispatcher.DispatcherProvider
import co.bitfuse.connecthub.data.feed.PostDao
import co.bitfuse.connecthub.data.mapper.toDomain
import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.BookmarkRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BookmarkRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val dispatchers: DispatcherProvider,
) : BookmarkRepository {
    override fun observeBookmarks(): Flow<List<Post>> {
        return postDao.observeBookmarkedPosts().map { posts ->
            posts.map { it.toDomain() }
        }
    }

    override suspend fun removeBookmark(postId: String) {
        withContext(dispatchers.io) {
            postDao.setBookmarkState(postId = postId, isBookmarked = false)
        }
    }
}
