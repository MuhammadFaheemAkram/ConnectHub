package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.data.comment.CommentApiService
import com.example.connecthub.data.comment.CommentDao
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.data.mapper.toEntity
import com.example.connecthub.domain.model.Comment
import com.example.connecthub.domain.repository.CommentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CommentRepositoryImpl @Inject constructor(
    private val commentApiService: CommentApiService,
    private val commentDao: CommentDao,
    private val postDao: PostDao,
    private val dispatchers: DispatcherProvider,
) : CommentRepository {
    override fun observeComments(postId: String): Flow<List<Comment>> {
        return commentDao.observeComments(postId).map { comments ->
            comments.map { it.toDomain() }
        }
    }

    override suspend fun refreshComments(postId: String) {
        withContext(dispatchers.io) {
            val comments = commentApiService.getComments(postId).map { it.toEntity() }
            commentDao.upsertComments(comments)
        }
    }

    override suspend fun addComment(postId: String, text: String): Comment = withContext(dispatchers.io) {
        val comment = commentApiService.addComment(postId = postId, text = text).toEntity()
        commentDao.upsertComment(comment)
        postDao.updateCommentCount(postId = postId, delta = 1)
        comment.toDomain()
    }

    override suspend fun deleteComment(commentId: String) {
        withContext(dispatchers.io) {
            val comment = commentDao.getComment(commentId) ?: return@withContext
            if (comment.isOwnComment) {
                commentDao.deleteOwnComment(commentId)
                postDao.updateCommentCount(postId = comment.postId, delta = -1)
            }
        }
    }
}
