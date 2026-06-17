package com.example.connecthub.feature.feed

data class FeedUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val posts: List<PostUiModel> = emptyList(),
    val errorMessage: String? = null,
    val canLoadMore: Boolean = true,
)
