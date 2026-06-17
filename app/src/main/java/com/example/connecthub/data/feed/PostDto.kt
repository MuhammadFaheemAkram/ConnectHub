package com.example.connecthub.data.feed

import com.example.connecthub.data.auth.UserDto

data class PostDto(
    val id: String,
    val author: UserDto,
    val content: String,
    val imageUrl: String?,
    val createdAt: Long,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
)
