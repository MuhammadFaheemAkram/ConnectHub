package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.User
import co.bitfuse.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<User> = profileRepository.observeProfile()
}
