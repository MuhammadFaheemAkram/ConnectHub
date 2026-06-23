package co.bitfuse.connecthub.domain.model

data class Conversation(
    val id: String,
    val participant: User,
    val lastMessage: String,
    val unreadCount: Int,
    val updatedAt: Long,
)
