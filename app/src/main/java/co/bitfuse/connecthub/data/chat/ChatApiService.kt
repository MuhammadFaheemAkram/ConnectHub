package co.bitfuse.connecthub.data.chat

interface ChatApiService {
    suspend fun getConversations(): List<ConversationDto>
    suspend fun getMessages(conversationId: String): List<MessageDto>
    suspend fun sendMessage(conversationId: String, text: String): MessageDto
}
