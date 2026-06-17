package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.BookmarkRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    operator fun invoke(): Flow<List<Post>> = bookmarkRepository.observeBookmarks()
}
