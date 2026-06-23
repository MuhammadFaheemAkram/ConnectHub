package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.FeedRepository
import javax.inject.Inject

class RefreshFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    suspend operator fun invoke(page: Int = 1): Boolean = feedRepository.refreshFeed(page = page)
}
