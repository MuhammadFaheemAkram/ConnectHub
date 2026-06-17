package com.example.connecthub.feature.postdetail

import com.example.connecthub.feature.comments.CommentUiModel
import com.example.connecthub.feature.feed.PostUiModel

data class PostDetailUiState(
    val isLoading: Boolean = true,
    val post: PostUiModel? = null,
    val commentsPreview: List<CommentUiModel> = emptyList(),
    val commentDraft: String = "",
    val isSubmittingComment: Boolean = false,
    val errorMessage: String? = null,
)
