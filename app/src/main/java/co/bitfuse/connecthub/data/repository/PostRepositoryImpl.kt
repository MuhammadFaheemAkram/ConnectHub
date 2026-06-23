package co.bitfuse.connecthub.data.repository

import co.bitfuse.connecthub.core.common.dispatcher.DispatcherProvider
import co.bitfuse.connecthub.data.feed.CreatePostRequestDto
import co.bitfuse.connecthub.data.feed.FeedApiService
import co.bitfuse.connecthub.data.feed.PostDao
import co.bitfuse.connecthub.data.mapper.toDomain
import co.bitfuse.connecthub.data.mapper.toEntity
import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.PostRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PostRepositoryImpl @Inject constructor(
    private val feedApiService: FeedApiService,
    private val postDao: PostDao,
    private val dispatchers: DispatcherProvider,
) : PostRepository {
    override fun observePost(postId: String): Flow<Post?> {
        return postDao.observePost(postId).map { it?.toDomain() }
    }

    override suspend fun refreshPost(postId: String) {
        withContext(dispatchers.io) {
            val cached = postDao.getPostById(postId)
            val remote = feedApiService.getPostDetails(postId)
            postDao.upsertPost(
                remote.toEntity(
                    page = cached?.page ?: 1,
                    isBookmarkedOverride = cached?.isBookmarked ?: remote.isBookmarked,
                ),
            )
        }
    }

    override suspend fun createPost(content: String, imageUrl: String?): Post = withContext(dispatchers.io) {
        val post = feedApiService.createPost(
            CreatePostRequestDto(
                content = content,
                imageUrl = imageUrl,
            ),
        ).toEntity(page = 1)
        postDao.upsertPost(post)
        post.toDomain()
    }
}
