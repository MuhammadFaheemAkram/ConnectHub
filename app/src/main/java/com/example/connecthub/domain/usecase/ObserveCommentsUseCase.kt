package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Comment
import com.example.connecthub.domain.repository.CommentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    operator fun invoke(postId: String): Flow<List<Comment>> = commentRepository.observeComments(postId)
}
