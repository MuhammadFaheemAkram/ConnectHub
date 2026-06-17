package com.example.connecthub.feature.bookmarks

import com.example.connecthub.bookmarks.FakeBookmarkRepository
import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.ObserveBookmarksUseCase
import com.example.connecthub.domain.usecase.RemoveBookmarkUseCase
import com.example.connecthub.feed.FeedTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarksViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state displays bookmarked posts`() = runTest {
        val repository = FakeBookmarkRepository(
            initialPosts = listOf(FeedTestData.post(id = "post-1", isBookmarked = true)),
        )
        val viewModel = viewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(listOf("post-1"), viewModel.uiState.value.posts.map { it.id })
    }

    @Test
    fun `remove bookmark delegates and updates state`() = runTest {
        val repository = FakeBookmarkRepository(
            initialPosts = listOf(FeedTestData.post(id = "post-1", isBookmarked = true)),
        )
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.removeBookmark("post-1")
        advanceUntilIdle()

        assertEquals("post-1", repository.removedPostId)
        assertEquals(emptyList<String>(), viewModel.uiState.value.posts.map { it.id })
    }

    private fun viewModel(repository: FakeBookmarkRepository): BookmarksViewModel {
        return BookmarksViewModel(
            observeBookmarksUseCase = ObserveBookmarksUseCase(repository),
            removeBookmarkUseCase = RemoveBookmarkUseCase(repository),
        )
    }
}
