package com.example.connecthub.data.notification

data class NotificationDto(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val postId: String?,
    val createdAt: Long,
)
