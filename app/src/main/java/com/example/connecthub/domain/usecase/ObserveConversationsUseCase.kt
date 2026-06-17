package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Conversation
import com.example.connecthub.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveConversationsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(): Flow<List<Conversation>> = chatRepository.observeConversations()
}
