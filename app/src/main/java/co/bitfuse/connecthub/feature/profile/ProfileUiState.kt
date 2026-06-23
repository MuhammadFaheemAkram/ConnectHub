package co.bitfuse.connecthub.feature.profile

import co.bitfuse.connecthub.domain.model.User
import co.bitfuse.connecthub.feature.feed.PostUiModel

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val posts: List<PostUiModel> = emptyList(),
    val errorMessage: String? = null,
)
