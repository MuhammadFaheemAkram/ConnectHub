package co.bitfuse.connecthub.feature.feed

import co.bitfuse.connecthub.core.common.util.TimeAgoFormatter
import co.bitfuse.connecthub.domain.model.Post

fun Post.toUiModel(
    nowMillis: Long = System.currentTimeMillis(),
): PostUiModel = PostUiModel(
    id = id,
    authorName = author.name,
    authorHeadline = author.bio.ifBlank { "ConnectHub member" },
    timeAgo = TimeAgoFormatter.format(nowMillis = nowMillis, createdAtMillis = createdAt),
    content = content,
    imageUrl = imageUrl,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    isBookmarked = isBookmarked,
)
