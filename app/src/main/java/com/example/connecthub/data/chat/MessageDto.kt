package com.example.connecthub.data.chat

data class MessageDto(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val text: String,
    val createdAt: Long,
    val isMine: Boolean,
)
