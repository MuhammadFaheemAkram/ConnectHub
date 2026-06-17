package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.FeedRepository
import javax.inject.Inject

class LikePostUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    suspend operator fun invoke(postId: String) {
        feedRepository.likePost(postId)
    }
}
