package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(content: String, imageUrl: String?): Post {
        return postRepository.createPost(
            content = content.trim(),
            imageUrl = imageUrl?.trim()?.takeIf { it.isNotBlank() },
        )
    }
}
