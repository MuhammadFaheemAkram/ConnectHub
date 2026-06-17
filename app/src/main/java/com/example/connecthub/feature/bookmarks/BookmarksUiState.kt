package com.example.connecthub.feature.bookmarks

import com.example.connecthub.feature.feed.PostUiModel

data class BookmarksUiState(
    val isLoading: Boolean = true,
    val posts: List<PostUiModel> = emptyList(),
    val errorMessage: String? = null,
)
