package co.bitfuse.connecthub.feature.bookmarks

import co.bitfuse.connecthub.feature.feed.PostUiModel

data class BookmarksUiState(
    val isLoading: Boolean = true,
    val posts: List<PostUiModel> = emptyList(),
    val errorMessage: String? = null,
)
