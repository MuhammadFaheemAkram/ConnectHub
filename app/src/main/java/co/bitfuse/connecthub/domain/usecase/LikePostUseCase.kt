package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.FeedRepository
import javax.inject.Inject

class LikePostUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    suspend operator fun invoke(postId: String) {
        feedRepository.likePost(postId)
    }
}
