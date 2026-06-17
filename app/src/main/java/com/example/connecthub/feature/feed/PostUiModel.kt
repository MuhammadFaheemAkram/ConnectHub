package com.example.connecthub.feature.feed

data class PostUiModel(
    val id: String,
    val authorName: String,
    val authorHeadline: String,
    val timeAgo: String,
    val content: String,
    val imageUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
)
