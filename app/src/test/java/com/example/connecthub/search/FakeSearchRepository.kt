package com.example.connecthub.search

import com.example.connecthub.domain.model.SearchResults
import com.example.connecthub.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeSearchRepository(
    private val results: SearchResults = SearchResults(),
) : SearchRepository {
    private val recentSearches = MutableStateFlow<List<String>>(emptyList())
    val searchedQueries = mutableListOf<String>()
    val savedQueries = mutableListOf<String>()
    var clearedRecentSearches = false

    override fun search(query: String): Flow<SearchResults> {
        searchedQueries += query
        return flowOf(results)
    }

    override fun observeRecentSearches(): Flow<List<String>> = recentSearches

    override suspend fun saveRecentSearch(query: String) {
        savedQueries += query
        recentSearches.value = (listOf(query) + recentSearches.value.filterNot { it == query }).take(10)
    }

    override suspend fun clearRecentSearches() {
        clearedRecentSearches = true
        recentSearches.value = emptyList()
    }

    fun seedRecentSearches(searches: List<String>) {
        recentSearches.value = searches
    }
}
