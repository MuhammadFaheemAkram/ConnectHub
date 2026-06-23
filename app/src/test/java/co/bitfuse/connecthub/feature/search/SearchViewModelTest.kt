package co.bitfuse.connecthub.feature.search

import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.domain.model.SearchResults
import co.bitfuse.connecthub.domain.usecase.ClearRecentSearchesUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveRecentSearchesUseCase
import co.bitfuse.connecthub.domain.usecase.SaveRecentSearchUseCase
import co.bitfuse.connecthub.domain.usecase.SearchUseCase
import co.bitfuse.connecthub.feed.FeedTestData
import co.bitfuse.connecthub.search.FakeSearchRepository
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
