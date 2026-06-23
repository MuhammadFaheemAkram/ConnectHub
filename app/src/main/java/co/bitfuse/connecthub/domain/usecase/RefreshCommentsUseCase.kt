package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.CommentRepository
import javax.inject.Inject

class RefreshCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(postId: String) {
        commentRepository.refreshComments(postId)
    }
}
