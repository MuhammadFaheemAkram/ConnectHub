package co.bitfuse.connecthub.data.repository

import co.bitfuse.connecthub.core.common.dispatcher.DispatcherProvider
import co.bitfuse.connecthub.data.comment.CommentApiService
import co.bitfuse.connecthub.data.comment.CommentDao
import co.bitfuse.connecthub.data.feed.PostDao
import co.bitfuse.connecthub.data.mapper.toDomain
import co.bitfuse.connecthub.data.mapper.toEntity
import co.bitfuse.connecthub.domain.model.Comment
import co.bitfuse.connecthub.domain.repository.CommentRepository
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
