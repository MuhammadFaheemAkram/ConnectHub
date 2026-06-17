package com.example.connecthub.feature.comments

import com.example.connecthub.core.common.util.TimeAgoFormatter
import com.example.connecthub.domain.model.Comment

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
