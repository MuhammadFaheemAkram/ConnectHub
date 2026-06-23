package co.bitfuse.connecthub.feature.comments

import co.bitfuse.connecthub.core.common.util.TimeAgoFormatter
import co.bitfuse.connecthub.domain.model.Comment

fun Comment.toUiModel(
    nowMillis: Long = System.currentTimeMillis(),
): CommentUiModel = CommentUiModel(
    id = id,
    authorName = author.name,
    authorHeadline = author.bio.ifBlank { "ConnectHub member" },
    text = text,
    timeAgo = TimeAgoFormatter.format(nowMillis = nowMillis, createdAtMillis = createdAt),
    isOwnComment = isOwnComment,
)
