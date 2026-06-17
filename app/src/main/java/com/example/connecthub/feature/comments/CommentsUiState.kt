package com.example.connecthub.feature.comments

data class CommentsUiState(
    val isLoading: Boolean = true,
    val comments: List<CommentUiModel> = emptyList(),
    val draft: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
)
