package co.bitfuse.connecthub.feature.postdetail

import co.bitfuse.connecthub.feature.comments.CommentUiModel
import co.bitfuse.connecthub.feature.feed.PostUiModel

data class PostDetailUiState(
    val isLoading: Boolean = true,
    val post: PostUiModel? = null,
    val commentsPreview: List<CommentUiModel> = emptyList(),
    val commentDraft: String = "",
    val isSubmittingComment: Boolean = false,
    val errorMessage: String? = null,
)
