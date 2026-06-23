package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Comment
import co.bitfuse.connecthub.domain.repository.CommentRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(postId: String, text: String): Comment {
        return commentRepository.addComment(postId = postId, text = text.trim())
    }
}
