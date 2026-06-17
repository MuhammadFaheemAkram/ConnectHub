package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.SearchResults
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String): Flow<SearchResults>
    fun observeRecentSearches(): Flow<List<String>>
    suspend fun saveRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
