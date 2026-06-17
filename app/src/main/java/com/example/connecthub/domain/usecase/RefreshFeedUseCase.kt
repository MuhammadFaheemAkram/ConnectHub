package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.FeedRepository
import javax.inject.Inject

class RefreshFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    suspend operator fun invoke(page: Int = 1): Boolean = feedRepository.refreshFeed(page = page)
}
