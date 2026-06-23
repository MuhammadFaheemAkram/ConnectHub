package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.PostRepository
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
