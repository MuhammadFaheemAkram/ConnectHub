package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.PostRepository
import javax.inject.Inject

class RefreshPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: String) {
        postRepository.refreshPost(postId)
    }
}
