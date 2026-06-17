package com.example.connecthub.feature.search

import com.example.connecthub.domain.model.User
import com.example.connecthub.feature.feed.PostUiModel

data class SearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList(),
    val users: List<User> = emptyList(),
    val posts: List<PostUiModel> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasQuery: Boolean = query.isNotBlank()
    val hasResults: Boolean = users.isNotEmpty() || posts.isNotEmpty()
}
