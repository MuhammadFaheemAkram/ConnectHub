package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.BookmarkRepository
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
