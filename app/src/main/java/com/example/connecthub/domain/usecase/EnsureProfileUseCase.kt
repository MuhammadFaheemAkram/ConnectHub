package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject

class EnsureProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        profileRepository.ensureProfile()
    }
}
