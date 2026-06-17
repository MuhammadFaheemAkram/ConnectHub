package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(commentId: String) {
        commentRepository.deleteComment(commentId)
    }
}
