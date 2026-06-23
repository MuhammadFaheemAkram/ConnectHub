package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.PostRepository
import javax.inject.Inject

class RefreshPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: String) {
        postRepository.refreshPost(postId)
    }
}
