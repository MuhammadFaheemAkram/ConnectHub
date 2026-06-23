package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.FeedRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    operator fun invoke(): Flow<List<Post>> = feedRepository.observeFeed()
}
