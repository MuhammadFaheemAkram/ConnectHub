package co.bitfuse.connecthub.feature.chatlist

data class ConversationUiModel(
    val id: String,
    val participantName: String,
    val participantHeadline: String,
    val lastMessage: String,
    val unreadCount: Int,
    val updatedAt: Long,
)

data class ChatListUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val conversations: List<ConversationUiModel> = emptyList(),
    val errorMessage: String? = null,
) {
    val visibleConversations: List<ConversationUiModel>
        get() = if (query.isBlank()) {
            conversations
        } else {
            conversations.filter {
                it.participantName.contains(query, ignoreCase = true) ||
                    it.lastMessage.contains(query, ignoreCase = true)
            }
        }
}
