package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.repository.BookmarkRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    operator fun invoke(): Flow<List<Post>> = bookmarkRepository.observeBookmarks()
}
