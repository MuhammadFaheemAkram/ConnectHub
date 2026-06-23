package co.bitfuse.connecthub.data.chat

import co.bitfuse.connecthub.data.auth.UserDto

data class ConversationDto(
    val id: String,
    val participant: UserDto,
    val lastMessage: String,
    val unreadCount: Int,
    val updatedAt: Long,
)
