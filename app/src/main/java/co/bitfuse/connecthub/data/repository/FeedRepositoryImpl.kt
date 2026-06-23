package co.bitfuse.connecthub.data.repository

import co.bitfuse.connecthub.core.common.dispatcher.DispatcherProvider
import co.bitfuse.connecthub.data.feed.FeedApiService
import co.bitfuse.connecthub.data.feed.PostDao
import co.bitfuse.connecthub.data.mapper.toDomain
import co.bitfuse.connecthub.data.mapper.toEntity
import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.FeedRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FeedRepositoryImpl @Inject constructor(
    private val feedApiService: FeedApiService,
    private val postDao: PostDao,
    private val dispatchers: DispatcherProvider,
) : FeedRepository {
    override fun observeFeed(): Flow<List<Post>> {
        return postDao.observeFeed().map { cachedPosts ->
            cachedPosts.map { it.toDomain() }
        }
    }

    override suspend fun refreshFeed(page: Int): Boolean = withContext(dispatchers.io) {
        val remotePosts = feedApiService.getFeed(page = page)
        val bookmarkedPostIds = postDao.getBookmarkedPostIds().toSet()
        val entities = remotePosts.map { post ->
            post.toEntity(
                page = page,
                isBookmarkedOverride = post.isBookmarked || post.id in bookmarkedPostIds,
            )
        }
        postDao.upsertPosts(entities)
        remotePosts.isNotEmpty()
    }

    override suspend fun likePost(postId: String) {
        withContext(dispatchers.io) {
            val cachedPost = postDao.getPostById(postId)
                ?: feedApiService.getPostDetails(postId).toEntity(page = 1).also { postDao.upsertPost(it) }
            val optimisticLiked = !cachedPost.isLiked
            postDao.updateLikeState(
                postId = postId,
                isLiked = optimisticLiked,
                likeCount = (cachedPost.likeCount + if (optimisticLiked) 1 else -1).coerceAtLeast(0),
            )

            val remotePost = feedApiService.likePost(postId)
            val isBookmarked = postDao.getBookmarkedPostIds().contains(postId) || cachedPost.isBookmarked
            postDao.upsertPost(remotePost.toEntity(page = cachedPost.page, isBookmarkedOverride = isBookmarked))
        }
    }

    override suspend fun bookmarkPost(postId: String) {
        withContext(dispatchers.io) {
            val cachedPost = postDao.getPostById(postId)
                ?: feedApiService.getPostDetails(postId).toEntity(page = 1).also { postDao.upsertPost(it) }
            val isBookmarked = !cachedPost.isBookmarked
            postDao.setBookmarkState(postId = postId, isBookmarked = isBookmarked)

            val remotePost = feedApiService.bookmarkPost(postId)
            postDao.upsertPost(
                remotePost.toEntity(
                    page = cachedPost.page,
                    isBookmarkedOverride = isBookmarked,
                ),
            )
            postDao.setBookmarkState(postId = postId, isBookmarked = isBookmarked)
        }
    }
}
