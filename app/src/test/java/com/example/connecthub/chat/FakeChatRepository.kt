package com.example.connecthub.chat

import com.example.connecthub.domain.model.Conversation
import com.example.connecthub.domain.model.Message
import com.example.connecthub.domain.model.User
import com.example.connecthub.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeChatRepository(
    initialConversations: List<Conversation> = listOf(sampleConversation()),
    initialMessages: List<Message> = listOf(sampleMessage()),
) : ChatRepository {
    private val conversations = MutableStateFlow(initialConversations)
    private val messages = MutableStateFlow(initialMessages)
    var refreshedConversations = false
    var refreshedMessagesFor: String? = null
    var sentText: String? = null

    override fun observeConversations(): Flow<List<Conversation>> = conversations

    override suspend fun refreshConversations() {
        refreshedConversations = true
    }

    override fun observeMessages(conversationId: String): Flow<List<Message>> = messages

    override suspend fun refreshMessages(conversationId: String) {
        refreshedMessagesFor = conversationId
    }

    override suspend fun sendMessage(conversationId: String, text: String) {
        sentText = text
        messages.value = messages.value + Message(
            id = "sent-message",
            conversationId = conversationId,
            senderId = "user-alex",
            text = text,
            createdAt = 2_000L,
            isMine = true,
        ) + Message(
            id = "reply-message",
            conversationId = conversationId,
            senderId = "user-maya",
            text = "Got it.",
            createdAt = 3_000L,
            isMine = false,
        )
    }

    companion object {
        fun sampleUser(): User = User(
            id = "user-maya",
            name = "Maya Chen",
            avatarUrl = "",
            bio = "Android Engineer",
            followersCount = 10,
            followingCount = 4,
        )

        fun sampleConversation(id: String = "conversation-1"): Conversation = Conversation(
            id = id,
            participant = sampleUser(),
            lastMessage = "Can you review this?",
            unreadCount = 1,
            updatedAt = 1_000L,
        )

        fun sampleMessage(id: String = "message-1"): Message = Message(
            id = id,
            conversationId = "conversation-1",
            senderId = "user-maya",
            text = "Can you review this?",
            createdAt = 1_000L,
            isMine = false,
        )
    }
}
