package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.PostRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(postId: String): Flow<Post?> = postRepository.observePost(postId)
}
