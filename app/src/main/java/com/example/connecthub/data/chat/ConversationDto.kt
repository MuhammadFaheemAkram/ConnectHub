package com.example.connecthub.data.chat

import com.example.connecthub.data.auth.UserDto

data class ConversationDto(
    val id: String,
    val participant: UserDto,
    val lastMessage: String,
    val unreadCount: Int,
    val updatedAt: Long,
)
