package com.example.connecthub.data.chat

import com.example.connecthub.core.network.FakeNetworkConfig
import com.example.connecthub.data.auth.UserDto
import javax.inject.Inject
import kotlinx.coroutines.delay

class FakeChatApiService @Inject constructor() : ChatApiService {
    private val conversations = seedConversations().toMutableList()
    private val messages = seedMessages().mapValues { it.value.toMutableList() }.toMutableMap()

    override suspend fun getConversations(): List<ConversationDto> {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()
        return conversations.sortedByDescending { it.updatedAt }
    }

    override suspend fun getMessages(conversationId: String): List<MessageDto> {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()
        return messages[conversationId].orEmpty().sortedBy { it.createdAt }
    }

    override suspend fun sendMessage(conversationId: String, text: String): MessageDto {
        delay(FakeNetworkConfig.defaultDelayMillis / 2)
        maybeThrowSimulatedError()
        val message = MessageDto(
            id = "message-${System.currentTimeMillis()}",
            conversationId = conversationId,
            senderId = currentUserId,
            text = text,
            createdAt = System.currentTimeMillis(),
            isMine = true,
        )
        messages.getOrPut(conversationId) { mutableListOf() }.add(message)
        updateConversation(conversationId, message.text, message.createdAt)
        return message
    }

    private fun updateConversation(
        conversationId: String,
        lastMessage: String,
        updatedAt: Long,
    ) {
        val index = conversations.indexOfFirst { it.id == conversationId }
        if (index >= 0) {
            conversations[index] = conversations[index].copy(
                lastMessage = lastMessage,
                unreadCount = 0,
                updatedAt = updatedAt,
            )
        }
    }

    private fun maybeThrowSimulatedError() {
        if (FakeNetworkConfig.errorSimulationEnabled) {
            error("Fake chat service is simulating a network failure.")
        }
    }

    private companion object {
        const val currentUserId = "user-alex"

        val maya = UserDto(
            id = "user-maya",
            name = "Maya Chen",
            email = "maya@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
            bio = "Android Engineer",
            followersCount = 8200,
            followingCount = 312,
        )
        val omar = UserDto(
            id = "user-omar",
            name = "Omar Malik",
            email = "omar@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
            bio = "Product Designer",
            followersCount = 5100,
            followingCount = 440,
        )
        val nadia = UserDto(
            id = "user-nadia",
            name = "Nadia Khan",
            email = "nadia@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
            bio = "Engineering Manager",
            followersCount = 12700,
            followingCount = 540,
        )

        fun seedConversations(): List<ConversationDto> {
            val now = System.currentTimeMillis()
            return listOf(
                ConversationDto("conversation-maya", maya, "Can you review the cache mapper?", 2, now - 8 * 60_000),
                ConversationDto("conversation-omar", omar, "The post card spacing feels good.", 0, now - 42 * 60_000),
                ConversationDto("conversation-nadia", nadia, "Let us sync after standup.", 1, now - 2 * 60 * 60_000),
            )
        }

        fun seedMessages(): Map<String, List<MessageDto>> {
            val now = System.currentTimeMillis()
            return mapOf(
                "conversation-maya" to listOf(
                    MessageDto("message-maya-1", "conversation-maya", "user-maya", "Can you review the cache mapper?", now - 10 * 60_000, false),
                    MessageDto("message-maya-2", "conversation-maya", currentUserId, "Yes, I will look after the app shell compiles.", now - 9 * 60_000, true),
                    MessageDto("message-maya-3", "conversation-maya", "user-maya", "Perfect, especially the entity mapper.", now - 8 * 60_000, false),
                ),
                "conversation-omar" to listOf(
                    MessageDto("message-omar-1", "conversation-omar", "user-omar", "The post card spacing feels good.", now - 42 * 60_000, false),
                ),
                "conversation-nadia" to listOf(
                    MessageDto("message-nadia-1", "conversation-nadia", "user-nadia", "Let us sync after standup.", now - 2 * 60 * 60_000, false),
                ),
            )
        }
    }
}
