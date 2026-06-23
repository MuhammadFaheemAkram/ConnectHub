package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.feed.FakeFeedRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FeedUseCaseTest {
    @Test
    fun `LikePostUseCase delegates to repository`() = runTest {
        val repository = FakeFeedRepository()

        LikePostUseCase(repository)("post-1")

        assertEquals("post-1", repository.likedPostId)
    }

    @Test
    fun `BookmarkPostUseCase delegates to repository`() = runTest {
        val repository = FakeFeedRepository()

        BookmarkPostUseCase(repository)("post-1")

        assertEquals("post-1", repository.bookmarkedPostId)
    }
}
