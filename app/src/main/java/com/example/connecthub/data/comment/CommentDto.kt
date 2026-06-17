package com.example.connecthub.data.comment

import com.example.connecthub.data.auth.UserDto

data class CommentDto(
    val id: String,
    val postId: String,
    val author: UserDto,
    val text: String,
    val createdAt: Long,
    val isOwnComment: Boolean,
)
