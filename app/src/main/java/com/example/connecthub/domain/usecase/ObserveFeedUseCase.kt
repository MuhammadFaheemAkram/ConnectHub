package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.FeedRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    operator fun invoke(): Flow<List<Post>> = feedRepository.observeFeed()
}
