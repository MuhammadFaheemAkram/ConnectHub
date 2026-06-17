package com.example.connecthub.feature.search

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.model.SearchResults
import com.example.connecthub.domain.usecase.ClearRecentSearchesUseCase
import com.example.connecthub.domain.usecase.ObserveRecentSearchesUseCase
import com.example.connecthub.domain.usecase.SaveRecentSearchUseCase
import com.example.connecthub.domain.usecase.SearchUseCase
import com.example.connecthub.feed.FeedTestData
import com.example.connecthub.search.FakeSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `search waits for debounce before querying repository`() = runTest {
        val repository = FakeSearchRepository(
            results = SearchResults(
                posts = listOf(FeedTestData.post(id = "post-1")),
            ),
        )
        val viewModel = viewModel(repository)

        viewModel.onQueryChange("compose")
        advanceTimeBy(349)

        assertTrue(repository.searchedQueries.isEmpty())

        advanceTimeBy(1)
        advanceUntilIdle()

        assertEquals(listOf("compose"), repository.savedQueries)
        assertEquals(listOf("compose"), repository.searchedQueries)
        assertEquals(listOf("post-1"), viewModel.uiState.value.posts.map { it.id })
        assertEquals(listOf("compose"), viewModel.uiState.value.recentSearches)
    }

    @Test
    fun `clear recent searches delegates to repository`() = runTest {
        val repository = FakeSearchRepository().apply {
            seedRecentSearches(listOf("android"))
        }
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.clearRecentSearches()
        advanceUntilIdle()

        assertTrue(repository.clearedRecentSearches)
        assertEquals(emptyList<String>(), viewModel.uiState.value.recentSearches)
    }

    private fun viewModel(repository: FakeSearchRepository): SearchViewModel {
        return SearchViewModel(
            searchUseCase = SearchUseCase(repository),
            observeRecentSearchesUseCase = ObserveRecentSearchesUseCase(repository),
            saveRecentSearchUseCase = SaveRecentSearchUseCase(repository),
            clearRecentSearchesUseCase = ClearRecentSearchesUseCase(repository),
        )
    }
}
