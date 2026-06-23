package co.bitfuse.connecthub.feature.comments

data class CommentUiModel(
    val id: String,
    val authorName: String,
    val authorHeadline: String,
    val text: String,
    val timeAgo: String,
    val isOwnComment: Boolean,
)
