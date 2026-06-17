package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.CommentRepository
import javax.inject.Inject

class RefreshCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(postId: String) {
        commentRepository.refreshComments(postId)
    }
}
