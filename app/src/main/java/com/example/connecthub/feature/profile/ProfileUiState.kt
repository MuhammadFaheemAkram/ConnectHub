package com.example.connecthub.feature.profile

import com.example.connecthub.domain.model.User
import com.example.connecthub.feature.feed.PostUiModel

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val posts: List<PostUiModel> = emptyList(),
    val errorMessage: String? = null,
)
