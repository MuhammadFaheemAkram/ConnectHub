package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.bookmarks.FakeBookmarkRepository
import co.bitfuse.connecthub.feed.FeedTestData
import co.bitfuse.connecthub.profile.FakeProfileRepository
import co.bitfuse.connecthub.search.FakeSearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class Phase5UseCaseTest {
    @Test
    fun `RemoveBookmarkUseCase delegates post id`() = runTest {
        val repository = FakeBookmarkRepository(
            initialPosts = listOf(FeedTestData.post(id = "post-1", isBookmarked = true)),
        )

        RemoveBookmarkUseCase(repository)("post-1")

        assertEquals("post-1", repository.removedPostId)
    }

    @Test
    fun `SaveRecentSearchUseCase stores query`() = runTest {
        val repository = FakeSearchRepository()

        SaveRecentSearchUseCase(repository)("compose")

        assertEquals(listOf("compose"), repository.observeRecentSearches().first())
    }

    @Test
    fun `UpdateProfileUseCase delegates edited fields`() = runTest {
        val repository = FakeProfileRepository()

        UpdateProfileUseCase(repository)(
            name = "Alex Updated",
            bio = "Updated bio",
            avatarUrl = "https://example.com/avatar.png",
        )

        assertEquals("Alex Updated", repository.updatedName)
        assertEquals("Updated bio", repository.updatedBio)
        assertEquals("https://example.com/avatar.png", repository.updatedAvatarUrl)
    }
}
