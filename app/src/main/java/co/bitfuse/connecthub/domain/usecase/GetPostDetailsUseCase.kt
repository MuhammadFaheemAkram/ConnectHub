package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.PostRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(postId: String): Flow<Post?> = postRepository.observePost(postId)
}
