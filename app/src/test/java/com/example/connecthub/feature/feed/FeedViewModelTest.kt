package com.example.connecthub.feature.feed

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.BookmarkPostUseCase
import com.example.connecthub.domain.usecase.LikePostUseCase
import com.example.connecthub.domain.usecase.ObserveFeedUseCase
import com.example.connecthub.domain.usecase.RefreshFeedUseCase
import com.example.connecthub.feed.FakeFeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial load refreshes feed successfully`() = runTest {
        val repository = FakeFeedRepository()
        val viewModel = viewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(listOf("post-1"), state.posts.map { it.id })
        assertTrue(state.canLoadMore)
    }

    @Test
    fun `initial refresh failure exposes error state`() = runTest {
        val repository = FakeFeedRepository().apply {
            shouldThrowOnRefresh = true
        }
        val viewModel = viewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Refresh failed", state.errorMessage)
        assertTrue(state.posts.isEmpty())
    }

    @Test
    fun `like action delegates to use case and updates feed state`() = runTest {
        val repository = FakeFeedRepository()
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.likePost("post-1")
        advanceUntilIdle()

        assertEquals("post-1", repository.likedPostId)
        assertTrue(viewModel.uiState.value.posts.first().isLiked)
    }

    @Test
    fun `bookmark action delegates to use case and updates feed state`() = runTest {
        val repository = FakeFeedRepository()
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.bookmarkPost("post-1")
        advanceUntilIdle()

        assertEquals("post-1", repository.bookmarkedPostId)
        assertTrue(viewModel.uiState.value.posts.first().isBookmarked)
    }

    private fun viewModel(repository: FakeFeedRepository): FeedViewModel {
        return FeedViewModel(
            observeFeedUseCase = ObserveFeedUseCase(repository),
            refreshFeedUseCase = RefreshFeedUseCase(repository),
            likePostUseCase = LikePostUseCase(repository),
            bookmarkPostUseCase = BookmarkPostUseCase(repository),
        )
    }
}
