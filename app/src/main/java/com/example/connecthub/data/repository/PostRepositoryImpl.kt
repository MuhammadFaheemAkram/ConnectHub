package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.data.feed.CreatePostRequestDto
import com.example.connecthub.data.feed.FeedApiService
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.data.mapper.toEntity
import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.PostRepository
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
