package com.example.connecthub.feature.feed

import com.example.connecthub.core.common.util.TimeAgoFormatter
import com.example.connecthub.domain.model.Post

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
