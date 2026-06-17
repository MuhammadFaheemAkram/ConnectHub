package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveProfilePostsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<List<Post>> = profileRepository.observeUserPosts()
}
