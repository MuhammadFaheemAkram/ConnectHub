package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject

class EnsureProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        profileRepository.ensureProfile()
    }
}
