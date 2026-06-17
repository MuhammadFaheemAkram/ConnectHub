package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.User
import com.example.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<User> = profileRepository.observeProfile()
}
