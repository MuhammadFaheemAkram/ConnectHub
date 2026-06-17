package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.Conversation
import com.example.connecthub.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeConversations(): Flow<List<Conversation>>
    suspend fun refreshConversations()
    fun observeMessages(conversationId: String): Flow<List<Message>>
    suspend fun refreshMessages(conversationId: String)
    suspend fun sendMessage(conversationId: String, text: String)
}
