package com.example.connecthub.domain.model

data class Post(
    val id: String,
    val author: User,
    val content: String,
    val imageUrl: String?,
    val createdAt: Long,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
)
