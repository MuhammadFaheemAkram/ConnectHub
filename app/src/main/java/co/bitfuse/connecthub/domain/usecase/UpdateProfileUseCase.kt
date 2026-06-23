package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(name: String, bio: String, avatarUrl: String) {
        profileRepository.updateProfile(
            name = name,
            bio = bio,
            avatarUrl = avatarUrl,
        )
    }
}
