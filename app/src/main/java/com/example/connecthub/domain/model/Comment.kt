package com.example.connecthub.domain.model

data class Comment(
    val id: String,
    val postId: String,
    val author: User,
    val text: String,
    val createdAt: Long,
    val isOwnComment: Boolean,
)
