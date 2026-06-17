package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.BookmarkRepository
import javax.inject.Inject

class RemoveBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(postId: String) {
        bookmarkRepository.removeBookmark(postId)
    }
}
