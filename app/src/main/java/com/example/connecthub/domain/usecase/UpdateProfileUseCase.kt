package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.ProfileRepository
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
