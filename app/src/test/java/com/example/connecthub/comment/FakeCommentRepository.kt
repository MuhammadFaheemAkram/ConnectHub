package com.example.connecthub.comment

import com.example.connecthub.domain.model.Comment
import com.example.connecthub.domain.model.User
import com.example.connecthub.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCommentRepository(
    initialComments: List<Comment> = emptyList(),
) : CommentRepository {
    private val comments = MutableStateFlow(initialComments)
    var shouldThrow = false
    var addedText: String? = null
    var deletedCommentId: String? = null

    override fun observeComments(postId: String): Flow<List<Comment>> = comments

    override suspend fun refreshComments(postId: String) {
        if (shouldThrow) error("Comments failed")
    }

    override suspend fun addComment(postId: String, text: String): Comment {
        if (shouldThrow) error("Add comment failed")
        addedText = text
        val comment = sampleComment(id = "new-comment", postId = postId, text = text, isOwnComment = true)
        comments.value = comments.value + comment
        return comment
    }

    override suspend fun deleteComment(commentId: String) {
        if (shouldThrow) error("Delete comment failed")
        deletedCommentId = commentId
        comments.value = comments.value.filterNot { it.id == commentId && it.isOwnComment }
    }

    companion object {
        fun sampleComment(
            id: String = "comment-1",
            postId: String = "post-1",
            text: String = "Helpful comment",
            isOwnComment: Boolean = false,
        ): Comment = Comment(
            id = id,
            postId = postId,
            author = User(
                id = "user-1",
                name = "Omar Malik",
                avatarUrl = "",
                bio = "Product Designer",
                followersCount = 10,
                followingCount = 4,
            ),
            text = text,
            createdAt = 1_000L,
            isOwnComment = isOwnComment,
        )
    }
}
