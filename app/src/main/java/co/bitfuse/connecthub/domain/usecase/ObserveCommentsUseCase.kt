package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Comment
import co.bitfuse.connecthub.domain.repository.CommentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    operator fun invoke(postId: String): Flow<List<Comment>> = commentRepository.observeComments(postId)
}
