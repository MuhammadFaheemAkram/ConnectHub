package co.bitfuse.connecthub.data.repository

import co.bitfuse.connecthub.core.common.dispatcher.DispatcherProvider
import co.bitfuse.connecthub.data.chat.ChatApiService
import co.bitfuse.connecthub.data.chat.ChatDao
import co.bitfuse.connecthub.data.chat.MessageEntity
import co.bitfuse.connecthub.data.mapper.toDomain
import co.bitfuse.connecthub.data.mapper.toEntity
import co.bitfuse.connecthub.domain.model.Conversation
import co.bitfuse.connecthub.domain.model.Message
import co.bitfuse.connecthub.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatRepositoryImpl @Inject constructor(
    private val chatApiService: ChatApiService,
    private val chatDao: ChatDao,
    private val dispatchers: DispatcherProvider,
) : ChatRepository {
    override fun observeConversations(): Flow<List<Conversation>> {
        return chatDao.observeConversations().map { conversations ->
            conversations.map { it.toDomain() }
        }
    }

    override suspend fun refreshConversations() {
        withContext(dispatchers.io) {
            chatDao.upsertConversations(
                chatApiService.getConversations().map { it.toEntity() },
            )
        }
    }

    override fun observeMessages(conversationId: String): Flow<List<Message>> {
        return chatDao.observeMessages(conversationId).map { messages ->
            messages.map { it.toDomain() }
        }
    }

    override suspend fun refreshMessages(conversationId: String) {
        withContext(dispatchers.io) {
            chatDao.upsertMessages(
                chatApiService.getMessages(conversationId).map { it.toEntity() },
            )
        }
    }

    override suspend fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        withContext(dispatchers.io) {
            val sent = chatApiService.sendMessage(conversationId, text).toEntity()
            chatDao.upsertMessage(sent)
            chatDao.updateConversationPreview(
                conversationId = conversationId,
                lastMessage = sent.text,
                updatedAt = sent.createdAt,
                unreadCount = 0,
            )
            delay(fakeReplyDelayMillis)
            val reply = buildReply(conversationId)
            chatDao.upsertMessage(reply)
            chatDao.updateConversationPreview(
                conversationId = conversationId,
                lastMessage = reply.text,
                updatedAt = reply.createdAt,
                unreadCount = 0,
            )
        }
    }

    private suspend fun buildReply(conversationId: String): MessageEntity {
        val conversation = chatDao.getConversation(conversationId)
        return MessageEntity(
            id = "reply-${System.currentTimeMillis()}",
            conversationId = conversationId,
            senderId = conversation?.participantId.orEmpty(),
            text = "Got it. I will take a look and follow up.",
            createdAt = System.currentTimeMillis(),
            isMine = false,
        )
    }

    private companion object {
        const val fakeReplyDelayMillis = 900L
    }
}
