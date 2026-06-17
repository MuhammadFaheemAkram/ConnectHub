package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Comment
import com.example.connecthub.domain.repository.CommentRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(postId: String, text: String): Comment {
        return commentRepository.addComment(postId = postId, text = text.trim())
    }
}
