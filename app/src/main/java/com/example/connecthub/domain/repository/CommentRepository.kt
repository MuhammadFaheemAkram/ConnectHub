package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun observeComments(postId: String): Flow<List<Comment>>
    suspend fun refreshComments(postId: String)
    suspend fun addComment(postId: String, text: String): Comment
    suspend fun deleteComment(commentId: String)
}
