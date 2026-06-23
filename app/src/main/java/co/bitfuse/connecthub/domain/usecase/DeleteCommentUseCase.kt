package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(commentId: String) {
        commentRepository.deleteComment(commentId)
    }
}
