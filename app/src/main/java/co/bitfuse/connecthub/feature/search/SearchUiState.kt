package co.bitfuse.connecthub.feature.search

import co.bitfuse.connecthub.domain.model.User
import co.bitfuse.connecthub.feature.feed.PostUiModel

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
