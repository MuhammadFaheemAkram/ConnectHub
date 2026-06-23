package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveProfilePostsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<List<Post>> = profileRepository.observeUserPosts()
}
